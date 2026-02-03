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
        // 판매글이 삭제된 경우(null) 처리
        if (post == null) {
            return new ChatRoomDetailResponse(
                    room.getId(),
                    room.getSeller().getUserId(),
                    room.getBuyer().getUserId(),
                    null,           // sellPostId
                    "삭제된 판매글입니다.", // postTitle
                    0L,             // postPrice
                    null,           // postCreatedAt
                    null,           // postUpdatedAt
                    null,           // thumbnail
                    "삭제됨"         // postStatus
            );
        }

        return new ChatRoomDetailResponse(
                room.getId(),
                room.getSeller().getUserId(),
                room.getBuyer().getUserId(),
                post.getId(),
                post.getTitle(),
                post.getPrice(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getThumbnail(),
                post.getStatus().getDescription()
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
