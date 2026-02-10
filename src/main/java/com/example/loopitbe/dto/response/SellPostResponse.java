package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;
import java.time.LocalDateTime;

public class SellPostResponse{
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SellPostResponse from(SellPost sellPost) {
        SellPostResponse response = new SellPostResponse();

        response.id = sellPost.getId();
        response.title = sellPost.getTitle();
        response.createdAt = sellPost.getCreatedAt();
        response.updatedAt = sellPost.getUpdatedAt();

        return response;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
