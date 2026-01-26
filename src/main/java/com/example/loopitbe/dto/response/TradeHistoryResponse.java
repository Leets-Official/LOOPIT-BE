package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.Transaction; // Transaction 임포트 추가
import java.time.LocalDateTime;

public record TradeHistoryResponse(
        Long postId,
        String thumbnailUrl,
        String title,
        Long price,
        String status,
        LocalDateTime createdAt
) {
    /**
     * 1. 판매글(SellPost) 엔티티로부터 DTO 생성
     */
    public static TradeHistoryResponse from(SellPost post, String thumbnailUrl) {
        return new TradeHistoryResponse(
                post.getId(),
                thumbnailUrl,
                post.getTitle(),
                post.getPrice(),
                post.getStatus(),
                post.getCreatedAt()
        );
    }

    /**
     * 2. 거래 내역(Transaction) 엔티티로부터 DTO 생성
     * 거래된 물품의 정보는 tx.getPost()를 통해 가져옵니다.
     */
    public static TradeHistoryResponse from(Transaction tx, String thumbnailUrl) {
        return new TradeHistoryResponse(
                tx.getPost().getId(),   // 거래된 게시글 ID
                thumbnailUrl,
                tx.getPost().getTitle(),
                tx.getPost().getPrice(),
                tx.getStatus(),         // 거래의 현재 상태 (구매중/완료 등)
                tx.getCreatedAt()       // 거래가 발생한 일시
        );
    }
}