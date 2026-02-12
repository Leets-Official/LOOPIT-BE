package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.ShopWishList;

public class ShopWishListResponse {
    private String shopName;
    private String location;
    private String phone;

    public static ShopWishListResponse from(ShopWishList shopWishList) {
        ShopWishListResponse response = new ShopWishListResponse();
        response.shopName = shopWishList.getShopName();
        response.location = shopWishList.getLocation();
        response.phone = shopWishList.getPhone();

        return response;
    }

    public String getShopName() {
        return shopName;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() { return phone; }
}
