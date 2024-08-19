package com.github.Hyun_jun_Lee0811.dictionary.service;

import com.github.Hyun_jun_Lee0811.dictionary.expection.ErrorResponse;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordBookEntryDTO;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordBookDTO;
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.User;
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.UserThink;
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.VisitorSummary;
import com.github.Hyun_jun_Lee0811.dictionary.repository.UserThinkRepository;
import com.github.Hyun_jun_Lee0811.dictionary.repository.UserRepository;
import com.github.Hyun_jun_Lee0811.dictionary.repository.VisitorSummaryRepository;
import com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordBookService {

  private final UserThinkRepository userThinkRepository;
  private final UserRepository userRepository;
  private final VisitorSummaryRepository visitorSummaryRepository;
  private final RedisTemplate<String, Long> redisTemplate;

  public void incrementVisitorCount(String username) {
    redisTemplate.opsForHash()
        .increment("visitor_counts", getUserIdByUsername(username).toString(), 1);
  }

  public List<WordBookDTO> getWordBookByUsernameAndWordId(String username, String wordId) {
    incrementVisitorCount(username);
    if (getFilteredUserThinks(getUserIdByUsername(username), wordId).isEmpty()) {
      throw new ErrorResponse(ErrorCode.NO_USERTHINKS_FOUND_OR_ACCESS_DENIED);
    }

    return convertToWordBookDTO(getFilteredUserThinks(getUserIdByUsername(username), wordId));
  }

  public boolean isUserAuthenticated(String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new ErrorResponse(ErrorCode.USER_NOT_AUTHENTICATED);
    }

    return username.equals(authentication.getName());
  }

  @Scheduled(cron = "0 0 0 * * *")
  private void syncDailyVisitorCounts() {
    for (Map.Entry<Object, Object> entry :
        redisTemplate.opsForHash().entries("visitor_counts").entrySet()) {

      VisitorSummary visitorSummary = visitorSummaryRepository.findByUserId(
              Long.parseLong((String) entry.getKey()))
          .orElse(new VisitorSummary(Long.parseLong((String) entry.getKey()), 0L));

      visitorSummary.setTotalCount(visitorSummary.getTotalCount() + (Long) entry.getValue());
      visitorSummaryRepository.save(visitorSummary);
    }

    redisTemplate.delete("visitor_counts");
  }

  private List<UserThink> getFilteredUserThinks(Long userId, String wordId) {
    return userThinkRepository.findByUserIdAndWordIdAndIsPrivate(userId, wordId, false);
  }

  private List<WordBookDTO> convertToWordBookDTO(List<UserThink> userThinks) {
    Map<String, WordBookDTO> wordBookMap = new HashMap<>();

    for (UserThink think : userThinks) {
      wordBookMap.computeIfAbsent(think.getWord(), word ->
          createWordBookDTO(think)).getThinks().add(createWordBookEntryDTO(think));
    }

    return new ArrayList<>(wordBookMap.values());
  }

  private WordBookDTO createWordBookDTO(UserThink think) {
    return WordBookDTO.builder()
        .word(think.getWord())
        .wordId(think.getWordId())
        .createdAt(think.getCreatedAt())
        .updatedAt(Optional.ofNullable(think.getUpdatedAt()).orElse(LocalDateTime.now()))
        .thinks(new ArrayList<>())
        .build();
  }

  private WordBookEntryDTO createWordBookEntryDTO(UserThink think) {
    return WordBookEntryDTO.builder()
        .id(think.getId())
        .userThink(think.getUserThink())
        .build();
  }

  private Long getUserIdByUsername(String username) {
    return userRepository.findByUsername(username)
        .map(User::getUserId)
        .orElseThrow(() -> new ErrorResponse(ErrorCode.USER_NOT_EXIT));
  }
}