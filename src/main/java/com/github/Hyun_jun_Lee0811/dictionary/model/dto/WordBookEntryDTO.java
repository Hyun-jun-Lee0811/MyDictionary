package com.github.Hyun_jun_Lee0811.dictionary.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordBookEntryDTO {

  private Long id;
  private String userThink;
}