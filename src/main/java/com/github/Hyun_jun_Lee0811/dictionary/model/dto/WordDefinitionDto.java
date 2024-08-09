package com.github.Hyun_jun_Lee0811.dictionary.model.dto;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WordDefinitionDto {

  private String id;
  private String word;
  private String attributionText;
  private String attributionUrl;
  private List<Map<String, Object>> citations;
  private List<Map<String, Object>> exampleUses;
  private String extendedText;
  private List<Map<String, Object>> labels;
  private List<Map<String, Object>> notes;
  private String partOfSpeech;
  private List<Map<String, Object>> relatedWords;
  private int score;
  private String seqString;
  private String sequence;
  private String sourceDictionary;
  private String text;
  private List<Map<String, Object>> textProns;

}