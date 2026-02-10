package com.example.loopitbe.dto.response;

public class IsShopInWishListResponse {
    private String shopName;
    private Boolean isShopInWishList;

    public IsShopInWishListResponse(String shopName, Boolean isShopInWishList) {
        this.shopName = shopName;
        this.isShopInWishList = isShopInWishList;
    }

    public String getShopName() {
        return shopName;
    }

    public Boolean getShopInWishList() {
        return isShopInWishList;
    }
}
