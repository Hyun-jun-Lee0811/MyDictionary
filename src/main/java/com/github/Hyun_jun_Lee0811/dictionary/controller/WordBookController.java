package com.github.Hyun_jun_Lee0811.dictionary.controller;

import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordBookDTO;
import com.github.Hyun_jun_Lee0811.dictionary.service.WordBookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wordbook")
@RequiredArgsConstructor
public class WordBookController {

  private final WordBookService wordBookService;

  @GetMapping("/search/{username}/{wordId}")
  public ResponseEntity<List<WordBookDTO>> getWordBookByUsernameAndWordId(
      @Valid @PathVariable("username") String username,
      @PathVariable("wordId") String wordId) {

    if (!wordBookService.isUserAuthenticated(username)) {
      return ResponseEntity.status(403).build();
    }
    return ResponseEntity.ok(wordBookService.getWordBookByUsernameAndWordId(username, wordId));
  }
}