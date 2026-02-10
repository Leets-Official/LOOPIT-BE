package com.example.loopitbe.dto.request;

public class ChatReadRequest {
    private Long roomId;

    public ChatReadRequest() {}

    public ChatReadRequest(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() { return roomId; }
}
