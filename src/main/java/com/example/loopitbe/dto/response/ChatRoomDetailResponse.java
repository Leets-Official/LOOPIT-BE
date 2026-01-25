package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.ChatRoom;

import java.time.LocalDateTime;

public class ChatRoomDetailResponse {
    private Long roomId;
    private Long sellerId;
    private Long buyerId;
    private Long sellPostId;
    private String postTitle;
    private Long postPrice;
    private LocalDateTime postCreatedAt;
    // 추후 이미지 필드 추가

    public ChatRoomDetailResponse() {}

    public ChatRoomDetailResponse(
            Long roomId, Long sellerId, Long buyerId,
            Long sellPostId, String postTitle, Long postPrice, LocalDateTime postCreatedAt
    ) {
        this.roomId = roomId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.sellPostId = sellPostId;
        this.postTitle = postTitle;
        this.postPrice = postPrice;
        this.postCreatedAt = postCreatedAt;
    }

    public static ChatRoomDetailResponse from(ChatRoom room) {
        return new ChatRoomDetailResponse(
                room.getId(),
                room.getSeller().getUserId(),
                room.getBuyer().getUserId(),
                room.getSellPost().getId(),
                room.getSellPost().getTitle(),
                room.getSellPost().getPrice(),
                room.getCreatedAt()
        );
    }

    public Long getRoomId() { return roomId; }
    public Long getSellerId() { return sellerId; }
    public Long getBuyerId() { return buyerId; }
    public Long getSellPostId() { return sellPostId; }
    public String getPostTitle() { return postTitle; }
    public Long getPostPrice() { return postPrice; }
    public LocalDateTime getPostCreatedAt() { return postCreatedAt; }
}
