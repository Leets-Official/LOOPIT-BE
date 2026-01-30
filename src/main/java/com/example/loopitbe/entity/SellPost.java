package com.example.loopitbe.entity;

import com.example.loopitbe.dto.request.SellPostRequest;
import com.example.loopitbe.enums.BatteryStatus;
import com.example.loopitbe.enums.PostStatus;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Entity
@Table(name = "sell_posts")
@EntityListeners(AuditingEntityListener.class)
public class SellPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long price;
    private String model;
    private String manufacturer;
    private String color;
    private String capacity;

    private boolean isUsed;
    private boolean hasScratch;
    private boolean isScreenCracked;

    @Enumerated(EnumType.STRING)
    private BatteryStatus batteryStatus;

    @OneToMany(mappedBy = "sellPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<PostImage> images = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    @Column(nullable = false)
    private String series;

    protected SellPost() {}

    private SellPost(User user, SellPostRequest dto, String series) {
        this.user = user;
        this.title = dto.getTitle();
        this.content = dto.getDescription();
        this.price = dto.getPrice();
        this.model = dto.getModel();
        this.manufacturer = dto.getManufacturer();
        this.color = dto.getColor();
        this.capacity = dto.getCapacity();
        this.isUsed = dto.isUsed();
        this.hasScratch = dto.isHasScratch();
        this.isScreenCracked = dto.isScreenCracked();
        this.batteryStatus = dto.getBatteryStatus();
        this.status = PostStatus.SALE;
        if (dto.getImageUrls() != null) {
            for (int i = 0; i < dto.getImageUrls().size(); i++) {
                this.images.add(new PostImage(dto.getImageUrls().get(i), i, this));
            }
        }
        // 시리즈 정보 저장
        this.series = series;
    }

    public static SellPost createPost(User user, SellPostRequest dto, String series) {
        return new SellPost(user, dto, series);
    }

    // 수정 메서드
    public void updatePost(SellPostRequest dto, String newSeries) {
        this.title = dto.getTitle();
        this.manufacturer = dto.getManufacturer();
        this.model = dto.getModel();
        this.series = newSeries;
        this.color = dto.getColor();
        this.capacity = dto.getCapacity();
        this.price = dto.getPrice();
        this.content = dto.getDescription();
        this.isUsed = dto.isUsed();
        this.hasScratch = dto.isHasScratch();
        this.isScreenCracked = dto.isScreenCracked();
        this.batteryStatus = dto.getBatteryStatus();
        this.images.clear();
        if (dto.getImageUrls() != null) {
            for (int i = 0; i < dto.getImageUrls().size(); i++) {
                this.images.add(new PostImage(dto.getImageUrls().get(i), i, this));
            }
        }
    }

    public void updateStatus(PostStatus status) {
        this.status = status;
    }

    public List<String> getImageUrlList() {
        return this.images.stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());
    }

    public String getThumbnail() {
        return (this.images != null && !this.images.isEmpty()) ? this.images.get(0).getImageUrl() : null;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getTitle() { return title; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getContent() { return content; }
    public Long getPrice() { return price; }
    public String getModel() { return model; }
    public String getManufacturer() { return manufacturer; }
    public String getColor() { return color; }
    public String getCapacity() { return capacity; }
    public PostStatus getStatus() { return status; } // 리턴 타입 변경
    public boolean isUsed() { return isUsed; }
    public boolean isHasScratch() { return hasScratch; }
    public boolean isScreenCracked() { return isScreenCracked; }
    public BatteryStatus getBatteryStatus() { return batteryStatus; }
    public String getSeries() { return series; }
    public List<PostImage> getImages() { return images; }
}