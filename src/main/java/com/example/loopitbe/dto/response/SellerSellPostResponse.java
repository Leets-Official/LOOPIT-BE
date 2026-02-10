package com.example.loopitbe.dto.response;

import org.springframework.data.domain.Page;

public class SellerSellPostResponse {
    private String sellerNickName;
    private String profileImg;
    Page<UserSellPostResponse> sellPosts;

    public SellerSellPostResponse(String sellerNickName, String profileImg, Page<UserSellPostResponse> sellPosts) {
        this.sellerNickName = sellerNickName;
        this.profileImg = profileImg;
        this.sellPosts = sellPosts;
    }

    public String getSellerNickName() {
        return sellerNickName;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public Page<UserSellPostResponse> getSellPosts() {
        return sellPosts;
    }
}
