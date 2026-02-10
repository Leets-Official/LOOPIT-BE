package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.Transaction;
import com.example.loopitbe.enums.TransactionStatus;

import java.time.LocalDateTime;

public class TransactionHistoryResponse {
    private Long postId;
    private String thumbnailUrl;
    private String title;
    private Long price;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TransactionHistoryResponse from(Transaction tr) {
        TransactionHistoryResponse response = new TransactionHistoryResponse();
        // 거래에 해당하는 판매글 가져오기
        SellPost post = tr.getSellPost();

        response.postId = post.getId();
        response.thumbnailUrl = post.getThumbnail();
        response.title = post.getTitle();
        response.price = post.getPrice();
        response.createdAt = post.getCreatedAt();
        response.updatedAt = post.getUpdatedAt();
        response.status = tr.getStatus();

        return response;
    }

    public Long getPostId() {
        return postId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public Long getPrice() {
        return price;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}