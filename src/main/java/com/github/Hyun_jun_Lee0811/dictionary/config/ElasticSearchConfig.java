package com.github.Hyun_jun_Lee0811.dictionary.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

  @Value("${aws.elasticsearch.url}")
  private String url;

  @Bean
  public ElasticsearchClient elasticsearchClient() {
    RestClientBuilder builder = RestClient.builder(HttpHost.create(url));
    RestClient Client = builder.build();
    RestClientTransport transport = new RestClientTransport(Client, new JacksonJsonpMapper());

    return new ElasticsearchClient(transport);
  }
}