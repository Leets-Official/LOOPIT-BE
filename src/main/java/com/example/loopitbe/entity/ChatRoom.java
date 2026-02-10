package com.example.loopitbe.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private SellPost sellPost;

    @Column(name = "last_message")
    private String lastMessage;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @CreationTimestamp // 생성 시간 자동 기록
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 기본 생성자
    protected ChatRoom() {}

    public ChatRoom(User buyer, User seller, SellPost sellPost) { // 추후 SellPost 객체 추가
        this.buyer = buyer;
        this.seller = seller;
        this.sellPost = sellPost;
    }

    // 마지막 메시지 업데이트 메서드 (비즈니스 로직)
    public void updateLastMessage(String message, LocalDateTime time) {
        this.lastMessage = message;
        this.lastMessageAt = time;
    }

    // Getters
    public Long getId() { return id; }
    public User getBuyer() { return buyer; }
    public User getSeller() { return seller; }
    public String getLastMessage() { return lastMessage; }
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public SellPost getSellPost() { return sellPost; }
}
