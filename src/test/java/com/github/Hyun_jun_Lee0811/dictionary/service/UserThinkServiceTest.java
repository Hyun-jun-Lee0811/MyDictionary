package com.github.Hyun_jun_Lee0811.dictionary.service;

import com.github.Hyun_jun_Lee0811.dictionary.client.WordnikClient;
import com.github.Hyun_jun_Lee0811.dictionary.expection.ErrorResponse;
import com.github.Hyun_jun_Lee0811.dictionary.model.UserThinkForm;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.UserThinkDTO;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordDefinitionDto;
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.User;
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.UserThink;
import com.github.Hyun_jun_Lee0811.dictionary.repository.UserThinkRepository;
import com.github.Hyun_jun_Lee0811.dictionary.repository.UserRepository;
import java.util.Collections;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserThinkServiceTest {

  @InjectMocks
  private UserThinkService userThinkService;

  @Mock
  private UserThinkRepository userThinkRepository;
  @Mock
  private WordnikClient wordnikClient;
  @Mock
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("자신의 생각 저장 성공")
  void saveUserThink_success() {
    UserThinkForm userThinkForm = new UserThinkForm();
    userThinkForm.setUsername("이현준");
    userThinkForm.setWord("apple");
    userThinkForm.setUserThink("good");
    userThinkForm.setIsPrivate(false);

    User user = new User();
    user.setUserId(1L);
    user.setUsername("이현준");

    WordDefinitionDto wordDefinitionDto = new WordDefinitionDto();
    wordDefinitionDto.setId("A5398300-1");

    when(userRepository.findByUsername("이현준")).thenReturn(Optional.of(user));
    when(wordnikClient.getDefinitions("apple")).thenReturn(
        Collections.singletonList(wordDefinitionDto));
    when(userThinkRepository.countByUserId(1L)).thenReturn(0L);

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        "이현준", null, Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    userThinkService.saveUserThink(userThinkForm);

    verify(userThinkRepository, times(1)).save(any(UserThink.class));
  }

  @Test
  @DisplayName("자신의 생각 저장 실패")
  void saveUserThink_fail() {
    when(mock(Authentication.class).getName()).thenReturn("이도현");
    when(mock(SecurityContext.class).getAuthentication()).
        thenReturn(mock(Authentication.class));
    SecurityContextHolder.setContext(mock(SecurityContext.class));

    UserThinkForm userThinkForm = new UserThinkForm();
    userThinkForm.setWord("apple");
    userThinkForm.setUserThink("good");
    userThinkForm.setIsPrivate(false);
    userThinkForm.setUsername("이현준");

    assertThrows(ErrorResponse.class, () -> userThinkService.saveUserThink(userThinkForm));
    verify(userThinkRepository, never()).save(any(UserThink.class));
  }


  @Test
  @DisplayName("자신의 생각 가져오기 성공")
  void getUserThoughts_Success() {
    UserThink think = new UserThink();
    think.setId(1L);
    think.setUserId(1L);
    think.setWord("apple");
    think.setUserThink("good");
    think.setIsPrivate(false);
    think.setCreatedAt(LocalDateTime.now());

    Pageable pageable = PageRequest.of(0, 10);
    Page<UserThink> page = new PageImpl<>(List.of(think), pageable, 1);

    User user = new User();
    user.setUserId(1L);
    user.setUsername("이현준");
    user.setPassword("password");

    when(userRepository.findByUsername("이현준")).thenReturn(Optional.of(user));
    when(userThinkRepository.findByUserId(1L, pageable)).thenReturn(page);

    Page<UserThinkDTO> result = userThinkService.getUserThoughts("이현준", pageable);

    assertEquals(1, result.getTotalElements());
    assertEquals("apple", result.getContent().get(0).getWord());
  }

  @Test
  @DisplayName("자신의 생각 가져오기 실패")
  void getUserThoughts_Fail() {
    when(userRepository.findByUsername("이현준")).thenReturn(Optional.empty());

    assertThrows(ErrorResponse.class, () ->
        userThinkService.getUserThoughts("이현준", PageRequest.of(0, 10)));
  }

  @Test
  @DisplayName("다른 사람 생각 가져오기 성공")
  void getPublicThoughts_Success() {
    UserThink think = new UserThink();
    think.setId(1L);
    think.setUserId(1L);
    think.setWord("apple");
    think.setUserThink("good");
    think.setIsPrivate(false);

    User user = new User();
    user.setUserId(1L);
    user.setUsername("이현준");
    user.setPassword("password");

    when(userRepository.findByUsername("이현준")).thenReturn(Optional.of(user));
    when(userThinkRepository.findByUserIdAndIsPrivate(1L, false)).thenReturn(List.of(think));

    List<UserThinkDTO> result = userThinkService.getPublicThoughts("이현준");

    assertEquals(1, result.size());
    assertEquals("good", result.get(0).getUserThink());
  }

  @Test
  @DisplayName("다른 사람 생각 가져오기 실패")
  void getPublicThoughts_Fail() {
    when(userRepository.findByUsername("이현준")).thenReturn(Optional.empty());

    assertThrows(ErrorResponse.class, () -> userThinkService.getPublicThoughts("이현준"));
  }

  @Test
  @DisplayName("다른 사람 특정 단어의 생각 가져오기 성공")
  void getUserThinksByWord_Success() {
    UserThink think = new UserThink();
    think.setId(1L);
    think.setUserId(1L);
    think.setWordId("C5097400-1");
    think.setWord("이현준");
    think.setUserThink("good");
    think.setIsPrivate(false);

    User user = new User();
    user.setUserId(1L);
    user.setUsername("이현준");
    user.setPassword("password");

    when(userRepository.findByUsername("이현준")).thenReturn(Optional.of(user));
    when(userThinkRepository.findByUserIdAndWordId(1L, "C5097400-1")).thenReturn(List.of(think));

    List<UserThinkDTO> result = userThinkService.getUserThinksByWord("이현준", "C5097400-1");

    assertEquals(1, result.size());
    assertEquals("good", result.get(0).getUserThink());
  }

  @Test
  @DisplayName("다른 사람 특정 단어의 생각 가져오기 실패")
  void getUserThinksByWord_Fail() {
    User user = new User();
    user.setUserId(1L);
    user.setUsername("이현준");

    when(userRepository.findByUsername("이현준")).thenReturn(Optional.of(user));
    when(userThinkRepository.findByUserIdAndWordId(1L, "C5097400-1")).thenReturn(
        Collections.emptyList());

    assertThrows(ErrorResponse.class,
        () -> userThinkService.getUserThinksByWord("이현준", "C5097400-1"));
  }

  @Test
  @DisplayName("자신의 생각 변경 성공")
  void changeUserThink_Success() {
    User user = new User();
    user.setUserId(1L);
    user.setUsername("이현준");

    UserThink think = new UserThink();
    think.setId(21L);
    think.setUserId(1L);
    think.setWord("apple");
    think.setUserThink("좋아");
    think.setIsPrivate(false);

    when(userThinkRepository.findById(21L)).thenReturn(Optional.of(think));
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    UserThinkForm form = new UserThinkForm();
    form.setUsername("이현준");
    form.setUserThink("변경");

    assertDoesNotThrow(() -> userThinkService.changeUserThink(21L, form));

    verify(userThinkRepository, times(1)).save(any(UserThink.class));
  }

  @Test
  @DisplayName("자신의 생각 변경 실패 - 생각 없음")
  void changeUserThink_Fail_NotFound() {
    when(userThinkRepository.findById(21L)).thenReturn(Optional.empty());

    UserThinkForm form = new UserThinkForm();
    form.setUsername("이현준");

    assertThrows(ErrorResponse.class, () -> userThinkService.changeUserThink(21L, form));

    verify(userThinkRepository, never()).save(any(UserThink.class));
  }

  @Test
  @DisplayName("자신의 생각 삭제 성공")
  void deleteUserThink_Success() {
    User user = new User();
    user.setUserId(1L);
    user.setUsername("이현준");
    user.setPassword("password");

    UserThink think = new UserThink();
    think.setId(1L);
    think.setUserId(1L);
    think.setWord("apple");
    think.setUserThink("good");
    think.setIsPrivate(false);

    when(userRepository.findByUsername("이현준")).thenReturn(Optional.of(user));
    when(userThinkRepository.findById(1L)).thenReturn(Optional.of(think));

    UserThinkService spyService = spy(userThinkService);
    doReturn(true).when(spyService).isUserAuthenticated("이현준");

    assertDoesNotThrow(() -> spyService.deleteUserThink("이현준", 1L));

    verify(userThinkRepository, times(1)).delete(any(UserThink.class));
  }

  @Test
  @DisplayName("자신의 생각 삭제 실패")
  void deleteUserThink_Fail() {
    when(userThinkRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ErrorResponse.class, () -> userThinkService.deleteUserThink("이현준", 1L));

    verify(userThinkRepository, never()).delete(any(UserThink.class));
  }
}