package com.github.Hyun_jun_Lee0811.dictionary.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordBookDTO {

  private String word;
  private String wordId;
  private List<WordBookEntryDTO> thinks;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
