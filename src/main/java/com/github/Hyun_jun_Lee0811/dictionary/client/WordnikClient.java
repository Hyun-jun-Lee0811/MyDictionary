package com.github.Hyun_jun_Lee0811.dictionary.client;

import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.EXAMPLES_API_CLIENT_ERROR;
import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.EXAMPLES_API_NETWORK_ERROR;
import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.EXAMPLES_API_SERVER_ERROR;

import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordDefinitionDto;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordEtymologyDto;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordExampleDto;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordPronunciationDto;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.WordRelatedWordDto;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class WordnikClient {

  @Value("${wordnik.api.key}")
  private String apiKey;

  private final RestTemplate restTemplate;

  public List<WordDefinitionDto> getDefinitions(String word) {
    URI uri = UriComponentsBuilder.fromHttpUrl(
            "https://api.wordnik.com/v4/word.json/{word}/definitions")
        .queryParam("api_key", apiKey)
        .buildAndExpand(word)
        .toUri();

    WordDefinitionDto[] response = restTemplate.getForObject(uri, WordDefinitionDto[].class);
    return response != null ? List.of(response) : Collections.emptyList();
  }

  public WordExampleDto getExamples(String word) {
    URI uri = UriComponentsBuilder.fromHttpUrl(
            "https://api.wordnik.com/v4/word.json/{word}/examples")
        .queryParam("api_key", apiKey)
        .buildAndExpand(word)
        .toUri();
    return getWordExampleDto(uri);
  }

  public List<WordRelatedWordDto> getRelatedWords(String word) {
    URI uri = UriComponentsBuilder.fromHttpUrl(
            "https://api.wordnik.com/v4/word.json/{word}/relatedWords")
        .queryParam("api_key", apiKey)
        .buildAndExpand(word)
        .toUri();

    WordRelatedWordDto[] response = restTemplate.getForObject(uri, WordRelatedWordDto[].class);
    return response != null ? List.of(response) : Collections.emptyList();
  }

  public List<WordPronunciationDto> getPronunciations(String word) {
    URI uri = UriComponentsBuilder.fromHttpUrl(
            "https://api.wordnik.com/v4/word.json/{word}/pronunciations")
        .queryParam("api_key", apiKey)
        .buildAndExpand(word)
        .toUri();

    WordPronunciationDto[] response = restTemplate.getForObject(uri, WordPronunciationDto[].class);
    return response != null ? List.of(response) : Collections.emptyList();
  }

  public List<WordEtymologyDto> getEtymologies(String word) {
    URI uri = UriComponentsBuilder.fromHttpUrl(
            "https://api.wordnik.com/v4/word.json/{word}/etymologies")
        .queryParam("api_key", apiKey)
        .buildAndExpand(word)
        .toUri();

    WordEtymologyDto[] response = restTemplate.getForObject(uri, WordEtymologyDto[].class);
    return response != null ? List.of(response) : Collections.emptyList();
  }

  private WordExampleDto getWordExampleDto(URI uri) {
    try {
      return restTemplate.getForObject(uri, WordExampleDto.class);
    } catch (HttpClientErrorException e) {
      System.err.println(EXAMPLES_API_CLIENT_ERROR);
    } catch (HttpServerErrorException e) {
      System.err.println(EXAMPLES_API_SERVER_ERROR);
    } catch (RestClientException e) {
      System.err.println(EXAMPLES_API_NETWORK_ERROR);
    }

    return null;
  }
}