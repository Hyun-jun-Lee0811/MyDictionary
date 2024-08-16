package com.github.Hyun_jun_Lee0811.dictionary.model;

import com.github.Hyun_jun_Lee0811.dictionary.model.dto.UserThinkDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserThinkForm {

  @NotBlank(message = "Username cannot be empty")
  private String username;

  @NotBlank(message = "WordId cannot be empty")
  private String wordId;

  @NotBlank(message = "Word cannot be empty")
  private String word;

  @NotBlank(message = "User think content is required")
  @Size(max = 1000, message = "User think content must be less than or equal to 1000 characters")
  private String userThink;

  private Boolean isPrivate = false;

  public UserThinkDTO toDTO() {
    return UserThinkDTO.builder()
        .username(this.username)
        .wordId(this.wordId)
        .word(this.word)
        .userThink(this.userThink)
        .isPrivate(this.isPrivate)
        .build();
  }
}