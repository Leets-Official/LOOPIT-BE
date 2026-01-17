package com.example.loopitbe.entity;

import com.example.loopitbe.dto.SellPostRequest; // DTO 임포트
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sell_posts")
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
        this.components = components != null ? components : new ArrayList<>();
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
    }

    public static SellPost createPost(User user, SellPostRequest dto) {
        return new SellPost(
                user,
                dto.title(),
                dto.description(), // DTO 필드명에 맞춰 매핑
                dto.price(),
                dto.modelName(),
                dto.manufacturer(),
                dto.color(),
                dto.capacity(),
                dto.components(),
                dto.imageUrls()
        );
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Long getPrice() { return price; }
    public String getModel() { return model; }
    public String getManufacturer() { return manufacturer; }
    public String getColor() { return color; }
    public String getCapacity() { return capacity; }
    public List<String> getComponents() { return components; }
    public List<String> getImageUrls() { return imageUrls; }
}