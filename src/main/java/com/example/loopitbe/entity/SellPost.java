package com.example.loopitbe.entity;

import com.example.loopitbe.dto.request.SellPostRequest;
import com.example.loopitbe.enums.BatteryStatus;
import com.example.loopitbe.enums.PostStatus;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

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

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    private List<String> imageUrls = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    protected SellPost() {}

    private SellPost(User user, SellPostRequest dto) {
        this.user = user;
        this.title = dto.getTitle();
        this.content = dto.getDescription();
        this.price = dto.getPrice();
        this.model = dto.getModelName();
        this.manufacturer = dto.getManufacturer();
        this.color = dto.getColor();
        this.capacity = dto.getCapacity();
        this.isUsed = dto.isUsed();
        this.hasScratch = dto.isHasScratch();
        this.isScreenCracked = dto.isScreenCracked();
        this.batteryStatus = dto.getBatteryStatus();

        this.status = PostStatus.SALE; // 초기값 설정
        this.imageUrls = dto.getImageUrls() != null ? dto.getImageUrls() : new ArrayList<>();
    }

    public static SellPost createPost(User user, SellPostRequest dto) {
        return new SellPost(user, dto);
    }

    public void updateStatus(PostStatus status) {
        this.status = status;
    }

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Long getPrice() { return price; }
    public String getModel() { return model; }
    public String getManufacturer() { return manufacturer; }
    public String getColor() { return color; }
    public String getCapacity() { return capacity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public PostStatus getStatus() { return status; } // 리턴 타입 변경
    public boolean isUsed() { return isUsed; }
    public boolean isHasScratch() { return hasScratch; }
    public boolean isScreenCracked() { return isScreenCracked; }
    public BatteryStatus getBatteryStatus() { return batteryStatus; }
    public List<String> getImageUrls() { return imageUrls; }
    public String getThumbnail() {
        return (imageUrls != null && !imageUrls.isEmpty()) ? imageUrls.get(0) : null;
    }
}