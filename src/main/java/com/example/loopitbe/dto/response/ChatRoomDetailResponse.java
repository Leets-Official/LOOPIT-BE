package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.ChatRoom;
import com.example.loopitbe.entity.SellPost;

import java.time.LocalDateTime;

public class ChatRoomDetailResponse {
    private Long roomId;
    private Long sellerId;
    private Long buyerId;
    private Long sellPostId;
    private String postTitle;
    private Long postPrice;
    private LocalDateTime postCreatedAt;
    private LocalDateTime postUpdatedAt;
    private String thumbnail;
    private String postStatus; // description

    public ChatRoomDetailResponse() {}

    public ChatRoomDetailResponse(
            Long roomId, Long sellerId, Long buyerId,
            Long sellPostId, String postTitle, Long postPrice, LocalDateTime postCreatedAt, LocalDateTime postUpdatedAt,
            String thumbnail, String postStatus
    ) {
        this.roomId = roomId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.sellPostId = sellPostId;
        this.postTitle = postTitle;
        this.postPrice = postPrice;
        this.postCreatedAt = postCreatedAt;
        this.postUpdatedAt = postUpdatedAt;
        this.thumbnail = thumbnail;
        this.postStatus = postStatus;
    }

    public static ChatRoomDetailResponse from(ChatRoom room) {
        SellPost post = room.getSellPost();

        // 판매글이 삭제된 경우
        String title = post.isDeleted() ? "[삭제된 게시글] " + post.getTitle() : post.getTitle();
        String status = post.isDeleted() ? "삭제됨" : post.getStatus().getDescription();

        return new ChatRoomDetailResponse(
                room.getId(),
                room.getSeller().getUserId(),
                room.getBuyer().getUserId(),
                post.getId(),
                title,
                post.getPrice(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getThumbnail(),
                status
        );
    }

    public Long getRoomId() { return roomId; }
    public Long getSellerId() { return sellerId; }
    public Long getBuyerId() { return buyerId; }
    public Long getSellPostId() { return sellPostId; }
    public String getPostTitle() { return postTitle; }
    public Long getPostPrice() { return postPrice; }
    public LocalDateTime getPostCreatedAt() { return postCreatedAt; }
    public LocalDateTime getPostUpdatedAt() { return postUpdatedAt; }
    public String getThumbnail() { return thumbnail; }
    public String getPostStatus() { return postStatus; }
}
