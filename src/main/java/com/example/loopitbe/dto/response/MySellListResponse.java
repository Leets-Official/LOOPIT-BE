package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.enums.PostStatus;
import com.example.loopitbe.enums.TransactionStatus;

import java.time.LocalDateTime;

public class MySellListResponse {
    private Long postId;
    private String thumbnailUrl;
    private String title;
    private Long price;
    private PostStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MySellListResponse from(SellPost post) {
        MySellListResponse response = new MySellListResponse();

        response.postId = post.getId();
        response.thumbnailUrl = post.getThumbnail();
        response.title = post.getTitle();
        response.price = post.getPrice();
        response.createdAt = post.getCreatedAt();
        response.updatedAt = post.getUpdatedAt();
        response.status = post.getStatus();

        return response;
    }

    public Long getPostId() {
        return postId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public Long getPrice() {
        return price;
    }

    public PostStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
