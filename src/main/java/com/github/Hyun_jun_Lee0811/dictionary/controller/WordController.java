package com.github.Hyun_jun_Lee0811.dictionary.controller;

import com.github.Hyun_jun_Lee0811.dictionary.model.dto.*;
import com.github.Hyun_jun_Lee0811.dictionary.service.WordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/word.json/{word}")
@RequiredArgsConstructor
public class WordController {

  private final WordService wordService;

  @GetMapping("/definitions")
  public List<WordDefinitionDto> getDefinitions(@Valid @PathVariable("word") String word) {
    return wordService.getDefinitions(word);
  }

  @GetMapping("/examples")
  public WordExampleDto getExamples(@Valid @PathVariable("word") String word) {
    return wordService.getExamples(word);
  }

  @GetMapping("/relatedWords")
  public List<WordRelatedWordDto> getRelatedWords(@Valid @PathVariable("word") String word) {
    return wordService.getRelatedWords(word);
  }

  @GetMapping("/pronunciations")
  public List<WordPronunciationDto> getPronunciations(@Valid @PathVariable("word") String word) {
    return wordService.getPronunciations(word);
  }

  @GetMapping("/etymologies")
  public List<WordEtymologyDto> getEtymologies(@Valid @PathVariable("word") String word) {
    return wordService.getEtymologies(word);
  }
}