package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;

import java.time.LocalDateTime;

public class SellPostWishListResponse {
    private Long postId;
    private String title;
    private Long price;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getPostId() {
        return postId;
    }

    public Long getPrice() { return price; }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Entity -> DTO
    public static SellPostWishListResponse from(SellPost post){
        SellPostWishListResponse response = new SellPostWishListResponse();

        response.postId =  post.getId();
        response.title =  post.getTitle();
        response.price =  post.getPrice();
        response.imageUrl = post.getThumbnail();
        response.createdAt = post.getCreatedAt();
        response.updatedAt = post.getUpdatedAt();

        return response;
    }
}
