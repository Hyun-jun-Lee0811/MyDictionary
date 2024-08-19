package com.github.Hyun_jun_Lee0811.dictionary.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visitor_summary", indexes =
    {@Index(name = "idx_user_id", columnList = "USER_ID")})
public class VisitorSummary {

  @Id
  @Column(name = "USER_ID", nullable = false)
  private Long userId;

  @Column(name = "TOTAL_COUNT", nullable = false)
  private Long totalCount;
}