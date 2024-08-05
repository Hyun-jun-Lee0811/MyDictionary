package com.github.Hyun_jun_Lee0811.dictionary.model;

import com.github.Hyun_jun_Lee0811.dictionary.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


public class UserForm {

  @Data
  public static class SignUp {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    public User toEntity() {
      return User.builder()
          .username(this.username)
          .password(this.password)
          .build();
    }
  }

  @Data
  public static class SignIn {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
  }

  @Data
  public static class ChangeUsername {

    @NotNull(message = "Old Username cannot be empty")
    private String oldUsername;

    @NotBlank(message = "New password cannot be empty")
    private String password;

    @NotNull(message = "New newUsername cannot be empty")
    private String newUsername;
  }


  @Data
  public static class ChangePassword {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Old password cannot be empty")
    private String oldPassword;

    @NotBlank(message = "New password cannot be empty")
    private String newPassword;
  }

  @Data
  public static class DeleteAccount {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
  }
}
