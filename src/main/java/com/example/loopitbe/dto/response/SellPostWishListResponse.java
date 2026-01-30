package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;

import java.time.LocalDateTime;

public class SellPostWishListResponse {
    private Long postId;
    private String title;
    private Long price;
    private String imageUrl;
    private LocalDateTime createdAt;

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

    // Entity -> DTO
    public static SellPostWishListResponse from(SellPost post){
        SellPostWishListResponse response = new SellPostWishListResponse();

        response.postId =  post.getId();
        response.title =  post.getTitle();
        response.price =  post.getPrice();
        // SellPost 엔티티에서의 이미지 관리가 post_images 테이블로 되어있어 임시로 No Image 처리. 추후 리펙토링 필요
        response.imageUrl = post.getImageUrls().isEmpty() ? "No Image" : post.getImageUrls().get(0);
        response.createdAt = post.getCreatedAt();

        return response;
    }
}
