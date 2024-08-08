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
public class WordExampleDto {

  @JsonProperty("examples")
  private List<ExampleDto> examples;

  @Getter
  @Setter
  private static class ExampleDto {

    @JsonProperty("provider")
    private ProviderDto provider;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("rating")
    private Integer rating;

    @JsonProperty("url")
    private String url;

    @JsonProperty("word")
    private String word;

    @JsonProperty("text")
    private String text;

    @JsonProperty("documentId")
    private Integer documentId;

    @JsonProperty("exampleId")
    private Integer exampleId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private String author;
  }

  @Getter
  @Setter
  private static class ProviderDto {

    @JsonProperty("id")
    private Integer id;
  }
}