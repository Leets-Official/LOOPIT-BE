package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;

import java.time.LocalDateTime;

public class UserSellPostResponse {
    private Long postId;
    private String title;
    private String imageUrl;
    private Long price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return imageUrl;
    }

    public Long getPrice() {
        return price;
    }


    public static UserSellPostResponse from(SellPost post){
        UserSellPostResponse response = new  UserSellPostResponse();

        response.postId = post.getId();
        response.title = post.getTitle();
        response.imageUrl = post.getThumbnail();
        response.price = post.getPrice();
        response.createdAt = post.getCreatedAt();
        response.updatedAt = post.getUpdatedAt();

        return response;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
