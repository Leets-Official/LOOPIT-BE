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

    // Long buyerId -> String buyerKakaoId로 변경
    @Column(nullable = false)
    private String buyerKakaoId;

    // Long sellerId -> String sellerKakaoId로 변경
    @Column(nullable = false)
    private String sellerKakaoId;

    private String status;
    private LocalDateTime createdAt;

    // Getter들
    public Long getId() { return id; }
    public SellPost getPost() { return post; }
    public String getBuyerKakaoId() { return buyerKakaoId; } // 추가
    public String getSellerKakaoId() { return sellerKakaoId; } // 추가
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // 생성 시점에 시간을 자동으로 넣어주는 기능 (필요시)
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}