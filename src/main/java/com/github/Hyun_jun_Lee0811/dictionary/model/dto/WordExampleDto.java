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
public class WordExampleDto {

  private List<ExampleDto> examples;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ExampleDto {

    private ProviderDto provider;
    private Integer year;
    private Integer rating;
    private String url;
    private String word;
    private String text;
    private Integer documentId;
    private Integer exampleId;
    private String title;
    private String author;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ProviderDto {

    private Integer id;
  }
}