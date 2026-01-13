package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.ChatMessage;
import com.example.loopitbe.enums.MessageType;

// server -> client 메시지 뿌릴 때 사용
public class ChatMessageResponse {
    private Long roomId;
    private Long senderId;
    private String senderNickname;
    private String content;
    private MessageType type;
    private String sendTime; // LocalDateTime -> String 포맷팅

    public ChatMessageResponse() {}

    public ChatMessageResponse(Long roomId, Long senderId, String senderNickname, String content, MessageType type, String sendTime) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.type = type;
        this.sendTime = sendTime;
    }

    // Entity -> DTO 변환 메서드
    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
                message.getChatRoom().getId(),
                message.getSender().getUserId(),
                message.getSender().getNickname(),
                message.getContent(),
                message.getMessageType(),
                message.getCreatedAt().toString() // 실제론 포맷팅 필요 (예: DateTimeFormatter)
        );
    }

    // Getters
    public Long getRoomId() { return roomId; }
    public Long getSenderId() { return senderId; }
    public String getSenderNickname() { return senderNickname; }
    public String getContent() { return content; }
    public MessageType getType() { return type; }
    public String getSendTime() { return sendTime; }
}
