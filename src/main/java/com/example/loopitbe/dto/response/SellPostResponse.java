package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;
import java.time.LocalDateTime;

public class SellPostResponse {
    private final Long id;
    private final String title;
    private final LocalDateTime createdAt;

    public SellPostResponse(Long id, String title, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    public static SellPostResponse from(SellPost sellPost) {
        return new SellPostResponse(
                sellPost.getId(),
                sellPost.getTitle(),
                sellPost.getCreatedAt()
        );
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}