package com.github.Hyun_jun_Lee0811.dictionary.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WordPronunciationDto {

  @JsonProperty("seq")
  private int seq;

  @JsonProperty("raw")
  private String raw;

  @JsonProperty("rawType")
  private String rawType;

  @JsonProperty("id")
  private String id;

  @JsonProperty("attributionText")
  private String attributionText;

  @JsonProperty("attributionUrl")
  private String attributionUrl;
}