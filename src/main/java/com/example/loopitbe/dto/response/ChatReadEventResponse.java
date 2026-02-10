package com.example.loopitbe.dto.response;

public class ChatReadEventResponse {
    private String type; // 항상 READ로 고정
    private Long roomId;
    private Long readerId; // 읽은 사람

    public ChatReadEventResponse() {}

    public ChatReadEventResponse(Long roomId, Long readerId) {
        this.type = "READ";
        this.roomId = roomId;
        this.readerId = readerId;
    }

    public String getType() { return type; }
    public Long getRoomId() { return roomId; }
    public Long getReaderId() { return readerId; }
}
