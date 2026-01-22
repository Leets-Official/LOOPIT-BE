package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.ChatMessage;
import com.example.loopitbe.enums.MessageType;

import java.time.format.DateTimeFormatter;

// server -> client 메시지 뿌릴 때 사용
public class ChatMessageResponse {
    private Long messageId;
    private Long roomId;
    private Long senderId;
    private String senderNickname;
    private String content;
    private MessageType type;
    private String sendTime; // LocalDateTime -> String 포맷팅
    private boolean isRead;

    public ChatMessageResponse() {}

    public ChatMessageResponse(Long messageId, Long roomId, Long senderId, String senderNickname, String content, MessageType type, String sendTime, boolean isRead) {
        this.messageId = messageId;
        this.roomId = roomId;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.content = content;
        this.type = type;
        this.sendTime = sendTime;
        this.isRead = isRead;
    }

    // Entity -> DTO 변환 메서드
    public static ChatMessageResponse from(ChatMessage message) {
        // sendTime 날짜 포맷팅
        String formattedTime = message.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return new ChatMessageResponse(
                message.getId(),
                message.getChatRoom().getId(),
                message.getSender().getUserId(),
                message.getSender().getNickname(),
                message.getContent(),
                message.getMessageType(),
                formattedTime,
                message.isRead()
        );
    }

    public boolean isRead() { return isRead; }

    // Getters
    public Long getRoomId() { return roomId; }
    public Long getSenderId() { return senderId; }
    public String getSenderNickname() { return senderNickname; }
    public String getContent() { return content; }
    public MessageType getType() { return type; }
    public String getSendTime() { return sendTime; }
}
