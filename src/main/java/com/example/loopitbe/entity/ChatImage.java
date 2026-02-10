package com.example.loopitbe.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_images")
public class ChatImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private ChatMessage message;

    @Column(name = "image_url", nullable = false, length = 1024)
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 기본 생성자
    protected ChatImage() {}

    public ChatImage(ChatMessage message, String imageUrl) {
        this.message = message;
        this.imageUrl = imageUrl;
    }

    // Getters
    public Long getId() { return id; }
    public ChatMessage getMessage() { return message; }
    public String getImageUrl() { return imageUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
