package com.github.Hyun_jun_Lee0811.dictionary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class DictionaryApplication {

  public static void main(String[] args) {
    SpringApplication.run(DictionaryApplication.class, args);
  }
}