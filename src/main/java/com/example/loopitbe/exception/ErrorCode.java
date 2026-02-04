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
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "허용되지 않은 접근입니다."),
    REDIS_PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 메시지 파싱 중 오류가 발생했습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    SELL_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "판매 게시글을 찾을 수 없습니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    GEMINI_REQUEST_ERROR(HttpStatus.BAD_GATEWAY, "제미나이 API와의 연결에서 에러가 발생했습니다."),
    GEMINI_INVALID_RESPONSE(HttpStatus.BAD_GATEWAY, "외부 견적 서비스 응답 처리 중 오류가 발생했습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않거나 삭제된 게시글입니다."),
    DEVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "기기를 찾을 수 없습니다."),
    IMAGE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "허용된 이미지 업로드 개수를 초과했습니다."),
    IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제에 실패했습니다."),
    INVALID_STATUS_VALUE(HttpStatus.BAD_REQUEST, "잘못된 거래내역 조회 상태값입니다."),
    TRANSACTION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "해당 판매글에 대해 진행 중이거나 완료된 거래가 이미 존재합니다."),
    ONGOING_TRANSACTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "현재 진행 중인 거래가 존재하지 않습니다."),
    ONGOING_OR_COMPLETED_TRANSACTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "현재 진행 중인 혹은 완료된 거래가 존재하지 않습니다."),
    NOT_THE_BUYER(HttpStatus.FORBIDDEN, "예악 중인 거래에 해당하는 구매자가 아닙니다."),
    POST_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 게시글입니다."),
    TRANSACTION_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 거래완료된 거래입니다.");

    private final HttpStatus status;
    private final String message;


    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}