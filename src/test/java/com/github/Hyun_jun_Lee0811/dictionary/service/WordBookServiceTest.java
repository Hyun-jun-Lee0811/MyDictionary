package com.github.Hyun_jun_Lee0811.dictionary.service;

import com.github.Hyun_jun_Lee0811.dictionary.expection.ErrorResponse;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordBookDTO;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordBookEntryDTO;
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.User;
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.UserThink;
import com.github.Hyun_jun_Lee0811.dictionary.repository.UserThinkRepository;
import com.github.Hyun_jun_Lee0811.dictionary.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WordBookServiceTest {

  @Mock
  private UserThinkRepository userThinkRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RedisTemplate<String, Long> redisTemplate;

  @Mock
  private HashOperations<String, Object, Object> hashOperations;

  @InjectMocks
  private WordBookService wordBookService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    SecurityContextHolder.clearContext();
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
  }

  @Test
  @DisplayName("단어장 조회 성공")
  public void getWordBookByUsernameAndWordId_Success() {

    User user = new User();
    user.setUserId(1L);

    UserThink userThink = new UserThink();
    userThink.setWord("hello");
    userThink.setWordId("A5398300-1");
    userThink.setUserId(1L);
    userThink.setCreatedAt(LocalDateTime.now());
    userThink.setUpdatedAt(null);
    userThink.setId(1L);
    userThink.setUserThink("test think");
    userThink.setIsPrivate(false);

    when(userRepository.findByUsername("이현준")).thenReturn(Optional.of(user));
    when(userThinkRepository.findByUserIdAndWordIdAndIsPrivate(1L, "A5398300-1", false))
        .thenReturn(Collections.singletonList(userThink));
    when(mock(Authentication.class).isAuthenticated()).thenReturn(true);
    when(mock(Authentication.class).getName()).thenReturn("이현준");
    when(mock(SecurityContext.class).getAuthentication()).thenReturn(mock(Authentication.class));
    when(hashOperations.increment(anyString(), anyString(), anyLong())).thenReturn(1L);

    SecurityContextHolder.setContext(mock(SecurityContext.class));

    List<WordBookDTO> result = wordBookService.getWordBookByUsernameAndWordId("이현준", "A5398300-1");
    WordBookDTO wordBookDTO = result.get(0);
    WordBookEntryDTO entryDTO = wordBookDTO.getThinks().get(0);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("hello", wordBookDTO.getWord());
    assertEquals("A5398300-1", wordBookDTO.getWordId());
    assertEquals(1, wordBookDTO.getThinks().size());
    assertEquals(1L, entryDTO.getId());
    assertEquals("test think", entryDTO.getUserThink());
  }

  @Test
  @DisplayName("단어장 조회 실패")
  void getWordBookByUsernameAndWordId_Fail() {

    User user = new User();
    user.setUserId(1L);

    when(userRepository.findByUsername("이현준")).thenReturn(Optional.of(user));

    when(userThinkRepository.findByUserIdAndWordIdAndIsPrivate(1L, "A5398300-1", false))
        .thenReturn(Collections.emptyList());
    when(hashOperations.increment(anyString(), anyString(), anyLong())).thenReturn(1L);

    assertThrows(ErrorResponse.class,
        () -> wordBookService.getWordBookByUsernameAndWordId("이현준", "A5398300-1"));
  }
}