package com.example.loopitbe.dto.request;

public class ChatReadRequest {
    private Long roomId;
    private Long userId; // 읽은 사람(나)

    public ChatReadRequest() {}

    public ChatReadRequest(Long roomId, Long userId) {
        this.roomId = roomId;
        this.userId = userId;
    }

    public Long getRoomId() { return roomId; }
    public Long getUserId() { return userId; }
}
