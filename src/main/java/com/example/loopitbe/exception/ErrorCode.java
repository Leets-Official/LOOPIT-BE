package com.example.loopitbe.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    DUPLICATED_KAKAO_ID(HttpStatus.CONFLICT, "중복된 카카오 아이디입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임 입니다."),
    KAKAO_AUTHENTICATED_FAILED(HttpStatus.BAD_REQUEST, "카카오 인증에 실패하였습니다."),
    JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "OAuth 인증 중 Json 파싱에 실패하였습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
