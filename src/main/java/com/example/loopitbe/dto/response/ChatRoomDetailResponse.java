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
    private String thumbnail;
    private String postStatus; // description

    public ChatRoomDetailResponse() {}

    public ChatRoomDetailResponse(
            Long roomId, Long sellerId, Long buyerId,
            Long sellPostId, String postTitle, Long postPrice, LocalDateTime postCreatedAt,
            String thumbnail, String postStatus
    ) {
        this.roomId = roomId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.sellPostId = sellPostId;
        this.postTitle = postTitle;
        this.postPrice = postPrice;
        this.postCreatedAt = postCreatedAt;
        this.thumbnail = thumbnail;
        this.postStatus = postStatus;
    }

    public static ChatRoomDetailResponse from(ChatRoom room) {
        return new ChatRoomDetailResponse(
                room.getId(),
                room.getSeller().getUserId(),
                room.getBuyer().getUserId(),
                room.getSellPost().getId(),
                room.getSellPost().getTitle(),
                room.getSellPost().getPrice(),
                room.getCreatedAt(),
                room.getSellPost().getThumbnail(),
                room.getSellPost().getStatus().getDescription()
        );
    }

    public Long getRoomId() { return roomId; }
    public Long getSellerId() { return sellerId; }
    public Long getBuyerId() { return buyerId; }
    public Long getSellPostId() { return sellPostId; }
    public String getPostTitle() { return postTitle; }
    public Long getPostPrice() { return postPrice; }
    public LocalDateTime getPostCreatedAt() { return postCreatedAt; }
    public String getThumbnail() { return thumbnail; }
    public String getPostStatus() { return postStatus; }
}
