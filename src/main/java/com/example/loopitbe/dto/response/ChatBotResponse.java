package com.example.loopitbe.dto.response;

public class ChatBotResponse    {
    private final String reply;

    public ChatBotResponse(String reply) { this.reply = reply; }

    public String getReply() { return reply; }
}
