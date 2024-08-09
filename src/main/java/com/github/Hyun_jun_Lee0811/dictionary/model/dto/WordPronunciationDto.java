package com.github.Hyun_jun_Lee0811.dictionary.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WordPronunciationDto {

  private int seq;
  private String raw;
  private String rawType;
  private String id;
  private String attributionText;
  private String attributionUrl;
}