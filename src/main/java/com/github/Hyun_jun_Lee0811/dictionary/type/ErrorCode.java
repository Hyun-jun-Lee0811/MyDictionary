package com.github.Hyun_jun_Lee0811.dictionary.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  ID_NOT_EXIT("아이디가 존재하지 않습니다."),
  USER_NOT_EXIT("사용자가 존재하지 않습니다.."),
  USER_NAME_ALREADY_EXIT("존재하는 사용자 이름입니다."),
  PASSWORD_UN_MATCH("비밀번호가 일치하지 않습니다."),
  UNABLE_TO_GET_CURRENT_USER_ID("현재 사용자 ID를 가져올 수 없습니다."),
  INVALID_JWT_SIGNATURE("잘못된 JWT 서명입니다."),
  EXPIRED_JWT_TOKEN("만료된 JWT 토큰입니다."),
  UNSUPPORTED_JWT_TOKEN("지원되지 않는 JWT 토큰입니다."),
  INVALID_JWT_TOKEN("JWT 토큰이 잘못되었습니다."),
  EXAMPLES_API_CLIENT_ERROR("API 호출 중 클라이언트 오류가 발생했습니다."),
  EXAMPLES_API_SERVER_ERROR("API 호출 중 서버 오류가 발생했습니다."),
  EXAMPLES_API_NETWORK_ERROR("API 호출 중 네트워크 오류가 발생했습니다.");

  private final String message;
}
