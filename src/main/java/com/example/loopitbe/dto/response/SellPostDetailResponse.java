package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class SellPostDetailResponse {
    // 판매글 기본 정보
    private Long id;
    private List<String> imageUrls;
    private String title;
    private Long price;
    private String manufacturer;
    private String model;
    private String color;
    private String content;
    private String capacity;
    private LocalDateTime createdAt;

    // 기기 상태 정보
    private boolean isUsed;
    private boolean hasScratch;
    private boolean isScreenCracked;
    private String batteryStatus; // Description 반환
    private String postStatus;    // Description 반환

    // 판매자 정보
    private String sellerNickname;
    private String sellerProfileImage;

    // 비슷한 상품 리스트
    private List<SimilarPostResponse> similarPosts;

    // 판매자가 올린 판매글 리스트
    private List<PostBySellerResponse> postsBySeller;

    public SellPostDetailResponse(SellPost post, List<SimilarPostResponse> similarPosts, List<PostBySellerResponse> postsBySeller) {
        User seller = post.getUser();

        this.id = post.getId();
        this.imageUrls = post.getImageUrls();
        this.title = post.getTitle();
        this.price = post.getPrice();
        this.manufacturer = post.getManufacturer();
        this.model = post.getModel();
        this.color = post.getColor();
        this.content = post.getContent();
        this.capacity = post.getCapacity();
        this.createdAt = post.getCreatedAt();

        this.isUsed = post.isUsed();
        this.hasScratch = post.isHasScratch();
        this.isScreenCracked = post.isScreenCracked();
        this.batteryStatus = post.getBatteryStatus().getDescription();
        this.postStatus = post.getStatus().getDescription();

        this.sellerNickname = seller.getNickname();
        this.sellerProfileImage = seller.getProfileImage();

        this.similarPosts = similarPosts;
        this.postsBySeller = postsBySeller;
    }

    // Getters
    public Long getId() { return id; }
    public List<String> getImageUrls() { return imageUrls; }
    public String getTitle() { return title; }
    public Long getPrice() { return price; }
    public String getManufacturer() { return manufacturer; }
    public String getModel() { return model; }
    public String getColor() { return color; }
    public String getContent() { return content; }
    public String getCapacity() { return capacity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isUsed() { return isUsed; }
    public boolean isHasScratch() { return hasScratch; }
    public boolean isScreenCracked() { return isScreenCracked; }
    public String getBatteryStatus() { return batteryStatus; }
    public String getPostStatus() { return postStatus; }
    public String getSellerNickname() { return sellerNickname; }
    public String getSellerProfileImage() { return sellerProfileImage; }
    public List<SimilarPostResponse> getSimilarPosts() { return similarPosts; }
    public List<PostBySellerResponse> getPostsBySeller() { return postsBySeller; }
}
