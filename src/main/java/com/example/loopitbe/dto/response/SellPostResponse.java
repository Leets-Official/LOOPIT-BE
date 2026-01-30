package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;
import java.time.LocalDateTime;

public record SellPostResponse(Long id, String title, LocalDateTime createdAt) {

    public static SellPostResponse from(SellPost sellPost) {
        return new SellPostResponse(
                sellPost.getId(),
                sellPost.getTitle(),
                sellPost.getCreatedAt()
        );
    }
}