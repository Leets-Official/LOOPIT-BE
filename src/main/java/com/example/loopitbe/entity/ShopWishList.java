package com.example.loopitbe.entity;

import com.example.loopitbe.dto.request.ShopWishlistToggleRequest;
import jakarta.persistence.*;

@Entity
public class ShopWishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String shopName;
    private String location;

    public ShopWishList(User user, ShopWishlistToggleRequest dto) {
        this.user = user;
        this.shopName = dto.getShopName();
        this.location = dto.getLocation();
    }

    protected ShopWishList() {

    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getShopName() {
        return shopName;
    }

    public String getLocation() {
        return location;
    }
}
