package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.Transaction;
import java.time.LocalDateTime;

public record TradeHistoryResponse(
        Long postId,
        String thumbnailUrl,
        String title,
        Long price,
        String status,
        LocalDateTime createdAt
) {

    public static TradeHistoryResponse from(SellPost post, String thumbnailUrl) {
        return new TradeHistoryResponse(
                post.getId(),
                thumbnailUrl,
                post.getTitle(),
                post.getPrice(),
                post.getStatus().getDescription(),
                post.getCreatedAt()
        );
    }

    public static TradeHistoryResponse from(Transaction tx, String thumbnailUrl) {
        return new TradeHistoryResponse(
                tx.getPost().getId(),
                thumbnailUrl,
                tx.getPost().getTitle(),
                tx.getPost().getPrice(),
                tx.getStatus().getDescription(),
                tx.getCreatedAt()
        );
    }
}