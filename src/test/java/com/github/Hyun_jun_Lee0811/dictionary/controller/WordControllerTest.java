package com.github.Hyun_jun_Lee0811.dictionary.controller;

import com.github.Hyun_jun_Lee0811.dictionary.model.dto.*;
import com.github.Hyun_jun_Lee0811.dictionary.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WordControllerTest {

  private MockMvc mockMvc;

  @Mock
  private WordService wordService;

  @InjectMocks
  private WordController wordController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(wordController).build();
  }

  @Test
  @DisplayName("단어 정의 조회 성공")
  void getDefinitions_Success() throws Exception {
    String word = "test";
    List<WordDefinitionDto> definitions = Collections.singletonList(
        new WordDefinitionDto(
            "1", word, "", "",
            Collections.emptyList(), Collections.emptyList(), "",
            Collections.emptyList(), Collections.emptyList(), "", Collections.emptyList(),
            10, "", "", "",
            "",
            Collections.emptyList()
        )
    );

    when(wordService.getDefinitions(word)).thenReturn(definitions);

    mockMvc.perform(get("/word.json/{word}/definitions", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].word").value(word));
  }

  @Test
  @DisplayName("단어 정의 조회 실패")
  void getDefinitions_Fail() throws Exception {
    String word = "nonexistent";

    when(wordService.getDefinitions(word)).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/word.json/{word}/definitions", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  @DisplayName("예제 조회 성공")
  void getExamples_Success() throws Exception {
    String word = "test";
    WordExampleDto example = new WordExampleDto(Collections.singletonList(
        new WordExampleDto.ExampleDto(
            new WordExampleDto.ProviderDto(1), 2024, 5, "", word,
            "", 1001, 2001, "",
            "Hyunjun Lee"
        )
    ));

    when(wordService.getExamples(word)).thenReturn(example);

    mockMvc.perform(get("/word.json/{word}/examples", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.examples[0].word").value(word));
  }

  @Test
  @DisplayName("예제 조회 실패")
  void getExamples_Fail() throws Exception {
    String word = "test";

    when(wordService.getExamples(word)).thenReturn(null);

    mockMvc.perform(get("/word.json/{word}/examples", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.examples").doesNotExist());
  }

  @Test
  @DisplayName("관련 단어 조회 성공")
  void getRelatedWords_Success() throws Exception {
    String word = "test";
    List<WordRelatedWordDto> relatedWords = Collections.singletonList(
        new WordRelatedWordDto("synonym", List.of("experiment", "trial"))
    );

    when(wordService.getRelatedWords(word)).thenReturn(relatedWords);

    mockMvc.perform(get("/word.json/{word}/relatedWords", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].relationshipType").value("synonym"));
  }

  @Test
  @DisplayName("관련 단어 조회 실패")
  void getRelatedWords_Fail() throws Exception {
    String word = "test";

    when(wordService.getRelatedWords(word)).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/word.json/{word}/relatedWords", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  @DisplayName("단어 발음 조회 성공")
  void getPronunciations_Success() throws Exception {
    String word = "test";
    List<WordPronunciationDto> pronunciations = Collections.singletonList(
        new WordPronunciationDto(1, "tɛst", "IPA", "123", "", "")
    );

    when(wordService.getPronunciations(word)).thenReturn(pronunciations);

    mockMvc.perform(get("/word.json/{word}/pronunciations", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].raw").value("tɛst"));
  }

  @Test
  @DisplayName("단어 발음 조회 실패")
  void getPronunciations_Fail() throws Exception {
    String word = "test";

    when(wordService.getPronunciations(word)).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/word.json/{word}/pronunciations", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  @DisplayName("단어 어원 조회 성공")
  void getEtymologies_Success() throws Exception {
    String word = "test";
    List<WordEtymologyDto> etymologies = Collections.singletonList(
        new WordEtymologyDto(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ety>[L. <ets>testis</ets>.  Cf. <er>Testament</er>, <er>Testify</er>.]</ety>\n")
    );

    when(wordService.getEtymologies(word)).thenReturn(etymologies);

    mockMvc.perform(get("/word.json/{word}/etymologies", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].etymologies").value(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ety>[L. <ets>testis</ets>.  Cf. <er>Testament</er>, <er>Testify</er>.]</ety>\n"));
  }

  @Test
  @DisplayName("단어 어원 조회 실패")
  void getEtymologies_Fail() throws Exception {
    String word = "nonexistent";

    when(wordService.getEtymologies(word)).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/word.json/{word}/etymologies", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  @DisplayName("자동 완성 조회 성공")
  void autocomplete_Success() throws Exception {
    String word = "test";
    List<String> suggestions = Collections.singletonList(word);

    when(wordService.autocomplete(word)).thenReturn(suggestions);

    mockMvc.perform(get("/word.json/autocomplete")
            .param("word", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0]").value(word));
  }

  @Test
  @DisplayName("자동 완성 조회 실패")
  void autocomplete_Fail() throws Exception {
    String word = "test";

    when(wordService.autocomplete(word)).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/word.json/autocomplete")
            .param("word", word)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }
}