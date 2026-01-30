package com.example.loopitbe.dto.response;

public class ChatBotHistoryResponse {
    // User 또는 Bot
    private final String role;
    private final String message;

    public ChatBotHistoryResponse(String role, String message) {
        this.role = role;
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }
}
