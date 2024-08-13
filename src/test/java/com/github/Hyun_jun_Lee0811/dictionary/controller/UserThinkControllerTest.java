package com.github.Hyun_jun_Lee0811.dictionary.controller;

import com.github.Hyun_jun_Lee0811.dictionary.expection.ErrorResponse;
import com.github.Hyun_jun_Lee0811.dictionary.model.UserThinkForm;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.UserThinkDTO;
import com.github.Hyun_jun_Lee0811.dictionary.service.UserThinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserThinkControllerTest {

  @Mock
  private UserThinkService userThinkService;

  @InjectMocks
  private UserThinkController userThinkController;

  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(userThinkController).build();
  }

  @Test
  @DisplayName("자신의 생각 저장 성공")
  public void saveUserThink_Success() throws Exception {
    String jsonContent = "{ "
        + "\"username\": \"이현준\", "
        + "\"wordId\": \"A5398300-1\", "
        + "\"word\": \"apple\", "
        + "\"userThink\": \"good\", "
        + "\"isPrivate\": false }";

    mockMvc.perform(post("/user-think/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isOk());

    verify(userThinkService, times(1)).saveUserThink(any(UserThinkForm.class));
  }

  @Test
  @DisplayName("자신의 생각 저장 실패")
  public void saveUserThink_Fail() throws Exception {
    String jsonContent = "{ "
        + "\"username\": \"이현준\", "
        + "\"wordId\": \"A5398300-1\", "
        + "\"word\": \"apple\", "
        + "\"userThink\": \"good\", "
        + "\"isPrivate\": false }";

    doThrow(new ErrorResponse(SERVICE_EXCEPTION)).when(userThinkService)
        .saveUserThink(any(UserThinkForm.class));

    mockMvc.perform(post("/user-think/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isInternalServerError());

    verify(userThinkService, times(1)).saveUserThink(any(UserThinkForm.class));
  }

  @Test
  @DisplayName("자신의 생각 가져오기 성공")
  public void getUserThoughts_Success() throws Exception {
    UserThinkDTO dto1 = new UserThinkDTO(18L, "이현준", "C5097400-1", "car", "good", false);
    UserThinkDTO dto2 = new UserThinkDTO(20L, "이현준", "A5398300-1", "apple", "good", false);

    Page<UserThinkDTO> page = new PageImpl<>(Arrays.asList(dto1, dto2), PageRequest.of(0, 10), 2);

    when(userThinkService.isUserAuthenticated("이현준")).thenReturn(true);
    when(userThinkService.getUserThoughts(eq("이현준"), any(Pageable.class))).thenReturn(page);

    mockMvc.perform(get("/user-think/이현준?page=0&size=10")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.content[0].id").value(18))
        .andExpect(jsonPath("$.content[1].word").value("apple"));

    verify(userThinkService, times(1)).isUserAuthenticated(eq("이현준"));
    verify(userThinkService, times(1)).getUserThoughts(eq("이현준"), any(Pageable.class));
  }

  @Test
  @DisplayName("자신의 생각 가져오기 실패")
  public void getUserThoughts_Fail() throws Exception {
    when(userThinkService.isUserAuthenticated("이현준")).thenReturn(false);

    mockMvc.perform(get("/user-think/이현준?page=0&size=10")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());

    verify(userThinkService, times(1)).isUserAuthenticated(eq("이현준"));
    verify(userThinkService, never()).getUserThoughts(anyString(),
        any(Pageable.class));
  }

  @Test
  @DisplayName("다른 사람 생각 가져오기 성공")
  public void getPublicThoughts_Success() throws Exception {
    UserThinkDTO dto1 = new UserThinkDTO(14L, "이도현", "C5097400-1", "car", "apppppp", false);
    UserThinkDTO dto2 = new UserThinkDTO(16L, "이도현", "C5097400-1", "car", "apppppp", false);
    when(userThinkService.getPublicThoughts(anyString())).thenReturn(Arrays.asList(dto1, dto2));

    mockMvc.perform(get("/user-think/public/이도현")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username").value("이도현"))
        .andExpect(jsonPath("$[1].word").value("car"));

    verify(userThinkService, times(1)).getPublicThoughts(eq("이도현"));
  }

  @Test
  @DisplayName("다른 사람 생각 가져오기 실패")
  public void getPublicThoughts_Fail() throws Exception {
    when(userThinkService.getPublicThoughts(anyString())).thenThrow(
        new ErrorResponse(SERVICE_EXCEPTION));

    mockMvc.perform(get("/user-think/public/이도현")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError());

    verify(userThinkService, times(1)).getPublicThoughts(eq("이도현"));
  }

  @Test
  @DisplayName("다른 사람 특정 단어의 생각 가져오기 성공")
  public void getUserThinksByWord_Success() throws Exception {
    UserThinkDTO dto1 = new UserThinkDTO(14L, "이도현", "C5097400-1", "car", "apppppp", false);
    UserThinkDTO dto2 = new UserThinkDTO(16L, "이도현", "C5097400-1", "car", "apppppp", false);
    when(userThinkService.getUserThinksByWord(anyString(), anyString())).thenReturn(
        Arrays.asList(dto1, dto2));

    mockMvc.perform(get("/user-think/word/이도현/C5097400-1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].wordId").value("C5097400-1"))
        .andExpect(jsonPath("$[1].userThink").value("apppppp"));

    verify(userThinkService, times(1)).getUserThinksByWord(eq("이도현"), eq("C5097400-1"));
  }

  @Test
  @DisplayName("다른 사람 특정 단어의 생각 가져오기 실패")
  public void getUserThinksByWord_Fail() throws Exception {
    when(userThinkService.getUserThinksByWord(anyString(), anyString())).thenThrow(
        new ErrorResponse(SERVICE_EXCEPTION));

    mockMvc.perform(get("/user-think/word/이도현/C5097400-1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError());

    verify(userThinkService, times(1)).getUserThinksByWord(eq("이도현"), eq("C5097400-1"));
  }

  @Test
  @DisplayName("자신의 생각 삭제 성공")
  public void deleteUserThink_Success() throws Exception {
    mockMvc.perform(delete("/user-think/delete/이현준/18")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(userThinkService, times(1)).deleteUserThink(eq("이현준"), eq(18L));
  }

  @Test
  @DisplayName("자신의 생각 삭제 실패")
  public void deleteUserThink_Fail() throws Exception {
    doThrow(new ErrorResponse(SERVICE_EXCEPTION)).when(userThinkService)
        .deleteUserThink(anyString(), anyLong());

    mockMvc.perform(delete("/user-think/delete/이현준/18")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError());

    verify(userThinkService, times(1)).deleteUserThink(eq("이현준"), eq(18L));
  }
}