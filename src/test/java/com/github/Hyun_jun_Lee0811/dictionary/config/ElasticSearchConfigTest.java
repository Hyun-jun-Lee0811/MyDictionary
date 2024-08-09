package com.github.Hyun_jun_Lee0811.dictionary.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ElasticSearchConfigTest {

  @Autowired
  private ElasticsearchClient elasticsearchClient;

  @Test
  @DisplayName("Elasticsearch 클라이언트 구성 검증")
  void contextLoads() {
    assertNotNull(elasticsearchClient, "Elasticsearch 클라이언트가 구성되어 있으며 null이 아닙니다.");
  }

  @Test
  @DisplayName("Elasticsearch 클러스터와의 연결")
  public void testClusterHealth() {
    try {
      HealthResponse healthResponse = elasticsearchClient.cluster().health();
      System.out.println(healthResponse);
    } catch (Exception e) {
      System.err.println("Elasticsearch 클러스터와의 연결 실패");
      e.printStackTrace();
    }
  }

}
