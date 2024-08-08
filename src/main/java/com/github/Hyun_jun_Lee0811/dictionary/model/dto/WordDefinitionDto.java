package com.github.Hyun_jun_Lee0811.dictionary.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

  @JsonProperty("id")
  private String id;

  @JsonProperty("word")
  private String word;

  @JsonProperty("attributionText")
  private String attributionText;

  @JsonProperty("attributionUrl")
  private String attributionUrl;

  @JsonProperty("citations")
  private List<Map<String, Object>> citations;

  @JsonProperty("exampleUses")
  private List<Map<String, Object>> exampleUses;

  @JsonProperty("extendedText")
  private String extendedText;

  @JsonProperty("labels")
  private List<Map<String, Object>> labels;

  @JsonProperty("notes")
  private List<Map<String, Object>> notes;

  @JsonProperty("partOfSpeech")
  private String partOfSpeech;

  @JsonProperty("relatedWords")
  private List<Map<String, Object>> relatedWords;

  @JsonProperty("score")
  private int score;

  @JsonProperty("seqString")
  private String seqString;

  @JsonProperty("sequence")
  private String sequence;

  @JsonProperty("sourceDictionary")
  private String sourceDictionary;

  @JsonProperty("text")
  private String text;

  @JsonProperty("textProns")
  private List<Map<String, Object>> textProns;

}
