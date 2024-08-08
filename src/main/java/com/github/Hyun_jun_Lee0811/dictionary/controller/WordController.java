package com.github.Hyun_jun_Lee0811.dictionary.controller;

import com.github.Hyun_jun_Lee0811.dictionary.model.dto.*;
import com.github.Hyun_jun_Lee0811.dictionary.service.WordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/word.json")
@RequiredArgsConstructor
public class WordController {

  private final WordService wordService;

  @GetMapping("/{word}/definitions")
  public List<WordDefinitionDto> getDefinitions(@Valid @PathVariable("word") String word) {
    return wordService.getDefinitions(word);
  }

  @GetMapping("/{word}/examples")
  public WordExampleDto getExamples(@Valid @PathVariable("word") String word) {
    return wordService.getExamples(word);
  }

  @GetMapping("/{word}/relatedWords")
  public List<WordRelatedWordDto> getRelatedWords(@Valid @PathVariable("word") String word) {
    return wordService.getRelatedWords(word);
  }

  @GetMapping("/{word}/pronunciations")
  public List<WordPronunciationDto> getPronunciations(@Valid @PathVariable("word") String word) {
    return wordService.getPronunciations(word);
  }

  @GetMapping("/{word}/etymologies")
  public List<WordEtymologyDto> getEtymologies(@Valid @PathVariable("word") String word) {
    return wordService.getEtymologies(word);
  }

  @GetMapping("/autocomplete")
  public ResponseEntity<List<String>> autocomplete(@Valid @RequestParam("word") String word) {
    List<String> suggestions = wordService.autocomplete(word);
    return ResponseEntity.ok(suggestions);
  }
}