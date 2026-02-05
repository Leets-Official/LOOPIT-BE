package com.example.loopitbe.dto.request;

public class CreateTransactionRequest {
    private Long buyerId;
    private Long postId;

    public Long getBuyerId() {
        return buyerId;
    }

    public Long getPostId() {
        return postId;
    }
}
