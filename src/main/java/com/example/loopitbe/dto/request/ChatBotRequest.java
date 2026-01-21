package com.example.loopitbe.dto.request;

public class ChatBotRequest {
    private String userId;
    private String message;

    public ChatBotRequest() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
