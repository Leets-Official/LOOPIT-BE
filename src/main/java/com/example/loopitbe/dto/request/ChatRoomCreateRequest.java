package com.example.loopitbe.dto.request;

public class ChatRoomCreateRequest {
    private Long sellerId;
    private Long buyerId;
    // SellPost 필드 추가

    public ChatRoomCreateRequest() {}

    public ChatRoomCreateRequest(Long sellerId, Long buyerId) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
    }

    public Long getSellerId() { return sellerId; }
    public Long getBuyerId() { return buyerId; }
}
