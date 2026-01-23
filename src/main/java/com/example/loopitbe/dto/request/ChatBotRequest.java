package com.example.loopitbe.dto.request;

public class ChatBotRequest {
    private Long userId;
    private String message;

    public ChatBotRequest() {}

    public Long getUserId() { return userId; }
    public String getMessage() { return message; }
}
