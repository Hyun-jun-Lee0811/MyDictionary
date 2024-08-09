package com.github.Hyun_jun_Lee0811.dictionary.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
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

  @Value("${aws.elasticsearch.username}")
  private String username;

  @Value("${aws.elasticsearch.password}")
  private String password;

  @Bean
  public ElasticsearchClient elasticsearchClient() {
    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(
        AuthScope.ANY,
        new UsernamePasswordCredentials(username, password));

    RestClientBuilder builder = RestClient.builder(HttpHost.create(url))
        .setHttpClientConfigCallback(httpClientBuilder ->
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

    RestClient client = builder.build();
    RestClientTransport transport = new RestClientTransport(client, new JacksonJsonpMapper());

    return new ElasticsearchClient(transport);
  }
}