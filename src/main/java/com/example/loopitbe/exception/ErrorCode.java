package com.example.loopitbe.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    DUPLICATED_KAKAO_ID(HttpStatus.CONFLICT, "중복된 카카오 아이디입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임 입니다."),
    JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "Json 파싱에 실패하였습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
