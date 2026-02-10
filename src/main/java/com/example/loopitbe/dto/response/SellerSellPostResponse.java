package com.example.loopitbe.dto.response;

import org.springframework.data.domain.Page;

public class SellerSellPostResponse {
    private Long sellerId;
    private String profileImg;
    Page<UserSellPostResponse> sellPosts;

    public SellerSellPostResponse(Long sellerId, String profileImg, Page<UserSellPostResponse> sellPosts) {
        this.sellerId = sellerId;
        this.profileImg = profileImg;
        this.sellPosts = sellPosts;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public Page<UserSellPostResponse> getSellPosts() {
        return sellPosts;
    }
}
