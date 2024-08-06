package com.github.Hyun_jun_Lee0811.dictionary.expection;

import com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse extends RuntimeException {

  private ErrorCode errorCode;
  private String message;

  public ErrorResponse(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
  }
}
