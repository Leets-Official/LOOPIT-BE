package com.example.loopitbe.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_images")
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    private int sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private SellPost sellPost;

    protected PostImage() {}

    public PostImage(String imageUrl, int sortOrder, SellPost sellPost) {
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.sellPost = sellPost;
    }

    // Getters
    public Long getId() { return id; }
    public String getImageUrl() { return imageUrl; }
    public int getSortOrder() { return sortOrder; }
    public SellPost getSellPost() { return sellPost; }
}
