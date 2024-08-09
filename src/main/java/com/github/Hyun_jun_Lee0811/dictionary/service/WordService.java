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
    String normalizedWord = normalizeWord(word);
    List<WordDefinitionDto> definitions = wordnikClient.getDefinitions(normalizedWord);
    updateTrie(normalizeWord(word));
    return definitions;
  }

  public WordExampleDto getExamples(String word) {
    String normalizedWord = normalizeWord(word);
    WordExampleDto examples = wordnikClient.getExamples(normalizedWord);
    updateTrie(normalizeWord(word));
    return examples;
  }

  public List<WordRelatedWordDto> getRelatedWords(String word) {
    String normalizedWord = normalizeWord(word);
    List<WordRelatedWordDto> relatedWords = wordnikClient.getRelatedWords(normalizedWord);
    updateTrie(normalizedWord);
    return relatedWords;
  }

  public List<WordPronunciationDto> getPronunciations(String word) {
    String normalizedWord = normalizeWord(word);
    List<WordPronunciationDto> pronunciations = wordnikClient.getPronunciations(normalizedWord);
    updateTrie(normalizeWord(word));
    return pronunciations;
  }

  public List<WordEtymologyDto> getEtymologies(String word) {
    String normalizedWord = normalizeWord(word);
    List<WordEtymologyDto> etymologies = wordnikClient.getEtymologies(normalizedWord);
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