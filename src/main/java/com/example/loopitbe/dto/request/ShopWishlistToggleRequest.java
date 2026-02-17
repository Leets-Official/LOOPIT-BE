package com.example.loopitbe.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ShopWishlistToggleRequest {
    @NotBlank
    private String shopName;
    @NotBlank
    private String location;
    private String phone;

    public String getShopName() {
        return shopName;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() { return phone; }
}
