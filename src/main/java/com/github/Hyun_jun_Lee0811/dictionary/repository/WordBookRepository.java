package com.github.Hyun_jun_Lee0811.dictionary.repository;

import com.github.Hyun_jun_Lee0811.dictionary.model.entity.WordBook;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordBookRepository extends JpaRepository<WordBook, Long> {

  Optional<WordBook> findByUserIdAndWord(Long userId, String word);
}