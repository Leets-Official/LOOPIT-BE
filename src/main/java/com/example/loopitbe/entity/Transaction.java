package com.example.loopitbe.entity;

import com.example.loopitbe.enums.PostStatus;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    // 통합된 PostStatus 사용
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    private LocalDateTime createdAt;

    protected Transaction() {}

    public Transaction(SellPost post, User buyer, User seller, PostStatus status) {
        this.post = post;
        this.buyer = buyer;
        this.seller = seller;
        this.status = status;
    }

    // 상태 업데이트 메서드
    public void updateStatus(PostStatus status) {
        this.status = status;
    }

    // Getter들
    public Long getId() { return id; }
    public SellPost getPost() { return post; }
    public User getBuyer() { return buyer; }
    public User getSeller() { return seller; }
    public PostStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}