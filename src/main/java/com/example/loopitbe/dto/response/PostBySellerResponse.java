package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;

import java.time.LocalDateTime;

public class PostBySellerResponse {
    private Long id;
    private String title;
    private Long price;
    private String thumbnail;
    private LocalDateTime createdAt;

    public PostBySellerResponse(Long id, String title, Long price, String thumbnail, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
    }

    public static PostBySellerResponse from(SellPost post) {
        return new PostBySellerResponse(
                post.getId(),
                post.getTitle(),
                post.getPrice(),
                post.getThumbnail(),
                post.getCreatedAt()
        );
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Long getPrice() { return price; }
    public String getThumbnail() { return thumbnail; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
