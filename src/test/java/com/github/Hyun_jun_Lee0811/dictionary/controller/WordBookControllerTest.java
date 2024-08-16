package com.github.Hyun_jun_Lee0811.dictionary.controller;

import com.github.Hyun_jun_Lee0811.dictionary.expection.ErrorResponse;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordBookDTO;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordBookEntryDTO;
import com.github.Hyun_jun_Lee0811.dictionary.service.WordBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
public class WordBookControllerTest {

  @Mock
  private WordBookService wordBookService;

  @InjectMocks
  private WordBookController wordBookController;

  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(wordBookController).build();
  }

  @Test
  @DisplayName("단어장 조회 성공")
  public void getWordBookByUsernameAndWordId_Success() throws Exception {

    WordBookDTO wordBookDTO = WordBookDTO.builder()
        .word("apple")
        .wordId("A5398300-2")
        .createdAt(LocalDateTime.of(2024, 8, 14, 21, 35, 0))
        .updatedAt(LocalDateTime.of(2024, 8, 14, 22, 45, 39))
        .thinks(Arrays.asList(
            WordBookEntryDTO.builder().id(122L).userThink("The apple is red.").build(),
            WordBookEntryDTO.builder().id(123L).userThink("He ate the apple, stalk and all")
                .build(),
            WordBookEntryDTO.builder().id(126L).userThink("apple pie and custard").build(),
            WordBookEntryDTO.builder().id(127L).userThink("Apples are very delicious.").build()
        ))
        .build();

    given(wordBookService.getWordBookByUsernameAndWordId("이현준", "A5398300-2"))
        .willReturn(Collections.singletonList(wordBookDTO));
    given(wordBookService.isUserAuthenticated("이현준")).willReturn(true);

    mockMvc.perform(get("/wordbook/search/이현준/A5398300-2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].word", is("apple")))
        .andExpect(jsonPath("$[0].wordId", is("A5398300-2")))
        .andExpect(jsonPath("$[0].thinks[0].id", is(122)))
        .andExpect(jsonPath("$[0].thinks[0].userThink", is("The apple is red.")));
  }

  @Test
  @DisplayName("단어장 조회 실패")
  public void getWordBookByUsernameAndWordId_Fail() throws Exception {

    given(wordBookService.isUserAuthenticated("이현준")).willReturn(true);
    given(wordBookService.getWordBookByUsernameAndWordId("이현준", "A5398300-2"))
        .willThrow(new ErrorResponse(NO_USERTHINKS_FOUND_OR_ACCESS_DENIED));

    mockMvc.perform(get("/wordbook/search/이현준/A5398300-2"))
        .andExpect(status().isNotFound());
  }
}