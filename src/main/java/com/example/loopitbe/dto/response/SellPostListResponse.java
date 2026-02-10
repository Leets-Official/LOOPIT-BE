package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;

import java.time.LocalDateTime;

public class SellPostListResponse {
    private Long id;
    private String title;
    private Long price;
    private String model;
    private String thumbnail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

    public SellPostListResponse() {}

    public SellPostListResponse(Long id, String title, Long price, String model, String thumbnail, LocalDateTime createdAt, LocalDateTime updatedAt, String status) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.model = model;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public static SellPostListResponse from(SellPost post) {
        return new SellPostListResponse(
                post.getId(),
                post.getTitle(),
                post.getPrice(),
                post.getModel(),
                post.getThumbnail(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getStatus().getDescription()
        );
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Long getPrice() { return price; }
    public String getModel() { return model; }
    public String getThumbnail() { return thumbnail; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getStatus() { return status; }
}