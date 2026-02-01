package com.example.loopitbe.dto.request;

public class CreateTransactionRequest {
    private Long buyerId;
    private Long postId;
    // request 요청하는 사용자 id
    private Long userId;

    public Long getBuyerId() {
        return buyerId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }
}
