package com.example.loopitbe.entity;

import com.example.loopitbe.enums.PostStatus;
import com.example.loopitbe.enums.TransactionStatus;
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
    private SellPost sellPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    // RESERVED(예약중), COMPLETED(거래완료), CANCELED(취소됨)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Transaction() {}

    public static Transaction createTransaction(SellPost post, User buyer, User seller) {
        Transaction tr = new Transaction();

        tr.sellPost = post;
        tr.buyer = buyer;
        tr.seller = seller;
        tr.status = TransactionStatus.RESERVED;
        tr.createdAt = LocalDateTime.now();

        return tr;
    }

    // 상태 업데이트 메서드
    public void updateStatus(TransactionStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    // Getter들
    public Long getId() { return id; }
    public SellPost getSellPost() { return sellPost; }
    public User getBuyer() { return buyer; }
    public User getSeller() { return seller; }
    public TransactionStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}