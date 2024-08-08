package com.github.Hyun_jun_Lee0811.dictionary.service;

import com.github.Hyun_jun_Lee0811.dictionary.client.WordnikClient;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {

  private final WordnikClient wordnikClient;

  public List<WordDefinitionDto> getDefinitions(String word) {
    return wordnikClient.getDefinitions(word);
  }

  public WordExampleDto getExamples(String word) {
    return wordnikClient.getExamples(word);
  }

  public List<WordRelatedWordDto> getRelatedWords(String word) {
    return wordnikClient.getRelatedWords(word);
  }

  public List<WordPronunciationDto> getPronunciations(String word) {
    return wordnikClient.getPronunciations(word);
  }

  public List<WordEtymologyDto> getEtymologies(String word) {
    return wordnikClient.getEtymologies(word);
  }
}