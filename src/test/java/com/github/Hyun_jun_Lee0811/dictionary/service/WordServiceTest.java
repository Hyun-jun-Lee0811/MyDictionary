package com.github.Hyun_jun_Lee0811.dictionary.service;

import com.github.Hyun_jun_Lee0811.dictionary.client.WordnikClient;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.*;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.collections4.Trie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordServiceTest {

  @Mock
  private WordnikClient wordnikClient;

  @Mock
  private Trie<String, Boolean> trie;

  @InjectMocks
  private WordService wordService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("단어 정의 조회 성공")
  void GetDefinitions_Success() {
    String word = "test";
    List<WordDefinitionDto> expectedDefinitions = Collections.singletonList(
        new WordDefinitionDto(
            "1", word, "", "",
            Collections.emptyList(), Collections.emptyList(), "",
            Collections.emptyList(), Collections.emptyList(), "", Collections.emptyList(),
            10, "", "", "",
            "",
            Collections.emptyList()
        )
    );

    when(wordnikClient.getDefinitions(word)).thenReturn(expectedDefinitions);

    assertEquals(expectedDefinitions, wordService.getDefinitions(word));
  }

  @Test
  @DisplayName("단어 정의 조회 실패")
  void GetDefinitions_Fail() {
    String word = "test";
    List<WordDefinitionDto> expectedDefinitions = Collections.singletonList(
        new WordDefinitionDto(
            "1", word, "", "",
            Collections.emptyList(), Collections.emptyList(), "",
            Collections.emptyList(), Collections.emptyList(), "", Collections.emptyList(),
            10, "", "", "",
            "",
            Collections.emptyList()
        )
    );

    when(wordnikClient.getDefinitions(word)).thenReturn(Collections.emptyList());

    assertNotEquals(expectedDefinitions, wordService.getDefinitions(word));
  }

  @Test
  @DisplayName("예제 조회 성공")
  void GetExamples_Success() {
    String word = "test";
    WordExampleDto example = new WordExampleDto(Collections.singletonList(
        new WordExampleDto.ExampleDto(
            new WordExampleDto.ProviderDto(1), 2024, 5, "", word,
            "", 1001, 2001, "",
            "Hyunjun Lee"
        )
    ));

    when(wordnikClient.getExamples(word)).thenReturn(example);

    assertEquals(example, wordService.getExamples(word));
  }

  @Test
  @DisplayName("예제 조회 실패")
  void GetExamples_Fail() {
    String word = "test";
    WordExampleDto example = new WordExampleDto(Collections.singletonList(
        new WordExampleDto.ExampleDto(
            new WordExampleDto.ProviderDto(1), 2024, 5, "", word,
            "", 1001, 2001, "",
            "Hyunjun Lee"
        )
    ));

    when(wordnikClient.getExamples(word)).thenReturn(new WordExampleDto(Collections.emptyList()));

    assertNotEquals(example, wordService.getExamples(word));
  }

  @Test
  @DisplayName("관련 단어 조회 성공")
  void GetRelatedWords_Success() {
    String word = "test";
    List<WordRelatedWordDto> relatedWords = Collections.singletonList(
        new WordRelatedWordDto("synonym", List.of("experiment", "trial"))
    );

    when(wordnikClient.getRelatedWords(word)).thenReturn(relatedWords);

    assertEquals(relatedWords, wordService.getRelatedWords(word));
  }

  @Test
  @DisplayName("관련 단어 조회 실패")
  void GetRelatedWords_Fail() {
    String word = "test";
    List<WordRelatedWordDto> relatedWords = Collections.singletonList(
        new WordRelatedWordDto("synonym", List.of("experiment", "trial"))
    );

    when(wordnikClient.getRelatedWords(word)).thenReturn(Collections.emptyList());

    assertNotEquals(relatedWords, wordService.getRelatedWords(word));
  }

  @Test
  @DisplayName("단어 발음 조회 성공")
  void GetPronunciations_Success() {
    String word = "test";
    List<WordPronunciationDto> pronunciations = Collections.singletonList(
        new WordPronunciationDto(1, "tɛst", "IPA", "123", "",
            "")
    );

    when(wordnikClient.getPronunciations(word)).thenReturn(pronunciations);

    assertEquals(pronunciations, wordService.getPronunciations(word));
  }

  @Test
  @DisplayName("단어 발음 조회 실패")
  void GetPronunciations_Fail() {
    String word = "test";
    List<WordPronunciationDto> pronunciations = Collections.singletonList(
        new WordPronunciationDto(1, "tɛst", "IPA", "123", "",
            "")
    );

    when(wordnikClient.getPronunciations(word)).thenReturn(Collections.singletonList(
        new WordPronunciationDto(2, "tɛst", "IPA", "124", "",
            "")
    ));

    assertNotEquals(pronunciations, wordService.getPronunciations(word));
  }

  @Test
  @DisplayName("단어 어원 조회 성공")
  void GetEtymologies_Success() {
    String word = "test";
    List<WordEtymologyDto> etymologies = Collections.singletonList(
        new WordEtymologyDto("From Old 'test'")
    );

    when(wordnikClient.getEtymologies(word)).thenReturn(etymologies);

    assertEquals(etymologies, wordService.getEtymologies(word));
  }

  @Test
  @DisplayName("단어 어원 조회 실패")
  void GetEtymologies_Fail() {
    String word = "test";
    List<WordEtymologyDto> etymologies = Collections.singletonList(
        new WordEtymologyDto("From Old 'test'")
    );

    when(wordnikClient.getEtymologies(word)).thenReturn(Collections.singletonList(
        new WordEtymologyDto("From New 'tests'.")
    ));

    assertNotEquals(etymologies, wordService.getEtymologies(word));
  }


  @Test
  @DisplayName("자동 완성 성공")
  void Autocomplete_Success() {
    String word = "test";
    SortedMap<String, Boolean> expectedMap = new TreeMap<>();
    expectedMap.put(word, Boolean.TRUE);

    when(trie.prefixMap(word)).thenReturn(expectedMap);

    assertEquals(Collections.singletonList(word), wordService.autocomplete(word));
  }

  @Test
  @DisplayName("자동 완성 실패")
  void testAutocomplete_Fail() {
    String word = "test";
    SortedMap<String, Boolean> differentMap = new TreeMap<>();
    differentMap.put("tests", true);

    when(trie.prefixMap(word)).thenReturn(differentMap);

    assertNotEquals(Collections.singletonList(word), wordService.autocomplete(word));
  }
}