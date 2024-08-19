package com.github.Hyun_jun_Lee0811.dictionary.repository;

import com.github.Hyun_jun_Lee0811.dictionary.model.entity.VisitorSummary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorSummaryRepository extends JpaRepository<VisitorSummary, Long> {

  Optional<VisitorSummary> findByUserId(Long userId);
}