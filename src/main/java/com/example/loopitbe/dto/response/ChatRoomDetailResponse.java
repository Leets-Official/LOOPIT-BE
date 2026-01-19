package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.ChatRoom;

public class ChatRoomDetailResponse {
    private Long roomId;
    private Long sellerId;
    private Long buyerId;

    public ChatRoomDetailResponse() {}

    public ChatRoomDetailResponse(Long roomId, Long sellerId, Long buyerId) {
        this.roomId = roomId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
    }

    public static ChatRoomDetailResponse from(ChatRoom room) {
        return new ChatRoomDetailResponse(
                room.getId(),
                room.getSeller().getUserId(),
                room.getBuyer().getUserId()
        );
    }

    public Long getRoomId() { return roomId; }
    public Long getSellerId() { return sellerId; }
    public Long getBuyerId() { return buyerId; }
}
