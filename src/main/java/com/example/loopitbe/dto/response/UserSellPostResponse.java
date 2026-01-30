package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;

import java.time.LocalDateTime;

public class UserSellPostResponse {
    private Long postId;
    private String title;
    private String imageUrl;
    private Long price;
    private LocalDateTime createdAt;

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
        // SellPost 엔티티에서의 이미지 관리가 post_images 테이블로 되어있어 임시로 No Image 처리. 추후 리펙토링 필요
        response.imageUrl = post.getThumbnail();
        response.price = post.getPrice();
        response.createdAt = post.getCreatedAt();

        return response;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
