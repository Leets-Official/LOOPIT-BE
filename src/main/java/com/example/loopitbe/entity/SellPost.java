package com.example.loopitbe.entity;

import com.example.loopitbe.dto.request.SellPostRequest;
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

    @ElementCollection
    @CollectionTable(name = "post_components", joinColumns = @JoinColumn(name = "post_id"))
    private List<String> components = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    private List<String> imageUrls = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String status; // "판매중", "예약중", "판매완료"

    // 1. 기본 생성자는 외부 접근 차단 (protected)
    protected SellPost() {}

    // 2. 내부에서만 사용할 생성자 (id까지 포함된 풀 생성자)
    private SellPost(User user, String title, String content, Long price, String model,
                     String manufacturer, String color, String capacity,
                     List<String> components, List<String> imageUrls) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.price = price;
        this.model = model;
        this.manufacturer = manufacturer;
        this.color = color;
        this.capacity = capacity;
        this.status = "판매중";
        this.components = components != null ? components : new ArrayList<>();
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
    }

    public static SellPost createPost(User user, SellPostRequest dto) {
        return new SellPost(
                user,
                dto.title(),
                dto.description(),
                dto.price(),
                dto.modelName(),
                dto.manufacturer(),
                dto.color(),
                dto.capacity(),
                dto.components(),
                dto.imageUrls()
        );
    }

    public void updateStatus(String status) { this.status = status; }
    public String getStatus() { return status; }
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getTitle() { return title; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getContent() { return content; }
    public Long getPrice() { return price; }
    public String getModel() { return model; }
    public String getManufacturer() { return manufacturer; }
    public String getColor() { return color; }
    public String getCapacity() { return capacity; }
    public List<String> getComponents() { return components; }
    public List<String> getImageUrls() { return imageUrls; }
    public String getThumbnail() { return (imageUrls != null && !imageUrls.isEmpty()) ? imageUrls.get(0) : null; }
}