package com.example.loopitbe.dto.request;

import java.util.List;

public class IsShopInWishListRequest {
    List<String> shopNames;
    Long userId;

    public List<String> getShopNames() {
        return shopNames;
    }

    public Long getUserId() {
        return userId;
    }
}
