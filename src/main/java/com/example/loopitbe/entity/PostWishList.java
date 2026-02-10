package com.example.loopitbe.entity;

import jakarta.persistence.*;

@Entity
public class PostWishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private SellPost sellPost;

    protected PostWishList() {}

    public PostWishList(User user, SellPost sellPost) {
        this.user = user;
        this.sellPost = sellPost;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public SellPost getSellPost() {
        return sellPost;
    }

}
