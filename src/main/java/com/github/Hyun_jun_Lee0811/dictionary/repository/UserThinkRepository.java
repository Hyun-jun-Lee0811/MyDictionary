package com.github.Hyun_jun_Lee0811.dictionary.repository;

import com.github.Hyun_jun_Lee0811.dictionary.model.entity.UserThink;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserThinkRepository extends JpaRepository<UserThink, Long> {

  List<UserThink> findByUserIdAndIsPrivate(Long userId, Boolean isPrivate);

  List<UserThink> findByUserIdAndWordId(Long userId, String wordId);

  Page<UserThink> findByUserId(Long userId, Pageable pageable);
}