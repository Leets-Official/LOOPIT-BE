package com.example.loopitbe.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    DUPLICATED_KAKAO_ID(HttpStatus.CONFLICT, "중복된 카카오 아이디입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임 입니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "중복된 이메일 입니다."),
    KAKAO_AUTHENTICATED_FAILED(HttpStatus.BAD_REQUEST, "카카오 인증에 실패하였습니다."),
    JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "OAuth 인증 중 Json 파싱에 실패하였습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 액세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "JWT 토큰이 비어있거나 잘못되었습니다."),
    REDIS_PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 메시지 파싱 중 오류가 발생했습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    GEMINI_REQUEST_ERROR(HttpStatus.BAD_GATEWAY, "제미나이 API와의 연결에서 에러가 발생했습니다."),
    GEMINI_INVALID_RESPONSE(HttpStatus.BAD_GATEWAY, "외부 견적 서비스 응답 처리 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;


    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}