package com.example.loopitbe.dto.request;

public class WishlistToggleRequest {
    private Long postId;
    private Long userId;

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }
}
