package com.github.Hyun_jun_Lee0811.dictionary.model.dto;

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

  private String relationshipType;
  private List<String> words;
}