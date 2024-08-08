package com.github.Hyun_jun_Lee0811.dictionary.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WordRelatedWordDto {

  @JsonProperty("relationshipType")
  private String relationshipType;

  @JsonProperty("words")
  private List<String> words;
}
