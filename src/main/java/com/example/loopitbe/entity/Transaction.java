package com.example.loopitbe.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private SellPost post;

    // String buyerKakaoId 대신 User 엔티티와 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    private String status;
    private LocalDateTime createdAt;

    // Getter들
    public Long getId() { return id; }
    public SellPost getPost() { return post; }
    public User getBuyer() { return buyer; } // 타입 변경
    public User getSeller() { return seller; } // 타입 변경
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}