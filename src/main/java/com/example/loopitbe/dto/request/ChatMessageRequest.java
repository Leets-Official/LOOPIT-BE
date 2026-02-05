package com.example.loopitbe.dto.request;

import com.example.loopitbe.enums.MessageType;

// client -> server 메시지 보낼 때 사용
public class ChatMessageRequest {
    private Long roomId;
    private String content;
    private MessageType type;

    // Jackson 역직렬화를 위한 기본 생성자
    public ChatMessageRequest() {}

    public ChatMessageRequest(Long roomId, String content, MessageType type) {
        this.roomId = roomId;
        this.content = content;
        this.type = type;
    }

    // Getters
    public Long getRoomId() { return roomId; }
    public String getContent() { return content; }
    public MessageType getType() { return type; }
}
