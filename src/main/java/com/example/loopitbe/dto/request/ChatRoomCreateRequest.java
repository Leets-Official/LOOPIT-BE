package com.example.loopitbe.dto.request;

public class ChatRoomCreateRequest {
    private Long sellerId;
    private Long buyerId;
    private Long sellPostId;

    public ChatRoomCreateRequest() {}

    public ChatRoomCreateRequest(Long sellerId, Long buyerId, Long sellPostId) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.sellPostId = sellPostId;
    }

    public Long getSellerId() { return sellerId; }
    public Long getBuyerId() { return buyerId; }
    public Long getSellPostId() { return sellPostId; }
}
