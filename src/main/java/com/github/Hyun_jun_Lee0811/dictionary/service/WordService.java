package com.github.Hyun_jun_Lee0811.dictionary.service;

import com.github.Hyun_jun_Lee0811.dictionary.client.WordnikClient;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {

  private final WordnikClient wordnikClient;
  private final Trie<String, Boolean> trie;

  public List<WordDefinitionDto> getDefinitions(String word) {
    List<WordDefinitionDto> definitions = wordnikClient.getDefinitions(normalizeWord(word));
    updateTrie(normalizeWord(word));
    return definitions;
  }

  public WordExampleDto getExamples(String word) {
    WordExampleDto examples = wordnikClient.getExamples(normalizeWord(word));
    updateTrie(normalizeWord(word));
    return examples;
  }

  public List<WordRelatedWordDto> getRelatedWords(String word) {
    List<WordRelatedWordDto> relatedWords = wordnikClient.getRelatedWords(normalizeWord(word));
    updateTrie(normalizeWord(word));
    return relatedWords;
  }

  public List<WordPronunciationDto> getPronunciations(String word) {
    List<WordPronunciationDto> pronunciations = wordnikClient.getPronunciations(
        normalizeWord(word));
    updateTrie(normalizeWord(word));
    return pronunciations;
  }

  public List<WordEtymologyDto> getEtymologies(String word) {
    List<WordEtymologyDto> etymologies = wordnikClient.getEtymologies(normalizeWord(word));
    updateTrie(normalizeWord(word));
    return etymologies;
  }

  public List<String> autocomplete(String word) {
    return trie.prefixMap(normalizeWord(word)).keySet()
        .stream()
        .toList();
  }

  private void updateTrie(String word) {
    trie.put(word, Boolean.TRUE);
  }

  private String normalizeWord(String word) {
    return word.toLowerCase();
  }
}