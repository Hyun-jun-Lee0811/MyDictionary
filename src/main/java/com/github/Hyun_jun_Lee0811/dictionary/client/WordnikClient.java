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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@RequiredArgsConstructor
public class WordnikClient {

  @Value("${wordnik.api.key}")
  private String apiKey;

  private final RestTemplate restTemplate;

  public List<WordDefinitionDto> getDefinitions(String word) {
    URI uri = buildUri("https://api.wordnik.com/v4/word.json/{word}/definitions", word);
    WordDefinitionDto[] response = executeRequest(uri, WordDefinitionDto[].class);
    return response != null ? List.of(response) : Collections.emptyList();
  }

  public WordExampleDto getExamples(String word) {
    URI uri = buildUri("https://api.wordnik.com/v4/word.json/{word}/examples", word);
    return executeRequest(uri, WordExampleDto.class);
  }

  public List<WordRelatedWordDto> getRelatedWords(String word) {
    URI uri = buildUri("https://api.wordnik.com/v4/word.json/{word}/relatedWords", word);
    WordRelatedWordDto[] response = executeRequest(uri, WordRelatedWordDto[].class);
    return response != null ? List.of(response) : Collections.emptyList();
  }

  public List<WordPronunciationDto> getPronunciations(String word) {
    URI uri = buildUri("https://api.wordnik.com/v4/word.json/{word}/pronunciations", word);
    WordPronunciationDto[] response = executeRequest(uri, WordPronunciationDto[].class);
    return response != null ? List.of(response) : Collections.emptyList();
  }

  public List<WordEtymologyDto> getEtymologies(String word) {
    URI uri = buildUri("https://api.wordnik.com/v4/word.json/{word}/etymologies", word);
    WordEtymologyDto[] response = executeRequest(uri, WordEtymologyDto[].class);
    return response != null ? List.of(response) : Collections.emptyList();
  }

  private URI buildUri(String urlTemplate, String word) {
    return UriComponentsBuilder.fromHttpUrl(urlTemplate)
        .queryParam("api_key", apiKey)
        .buildAndExpand(word)
        .toUri();
  }

  private <T> T executeRequest(URI uri, Class<T> responseType) {
    try {
      return restTemplate.getForObject(uri, responseType);
    } catch (HttpClientErrorException e) {
      log.error(String.valueOf(EXAMPLES_API_CLIENT_ERROR));
    } catch (HttpServerErrorException e) {
      log.error(String.valueOf(EXAMPLES_API_SERVER_ERROR));
    } catch (RestClientException e) {
      log.error(String.valueOf(EXAMPLES_API_NETWORK_ERROR));
    }
    return null;
  }
}