package com.example.loopitbe.dto.request;

public class ShopWishlistToggleRequest {
    private String shopName;
    private String location;
    private Long userId;

    public String getShopName() {
        return shopName;
    }

    public String getLocation() {
        return location;
    }

    public Long getUserId() {
        return userId;
    }
}
