package com.example.loopitbe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sell_posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public SellPost(User user, String title, String content, Long price, String model,
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
        this.components = components;
        this.imageUrls = imageUrls;
    }
}