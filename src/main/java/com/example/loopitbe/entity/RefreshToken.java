package com.example.loopitbe.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    protected RefreshToken() {}

    public static RefreshToken create(
            User user,
            String token,
            LocalDateTime expiresAt
    ) {
        RefreshToken rt = new RefreshToken();
        rt.user = user;
        rt.token = token;
        rt.expiresAt = expiresAt;
        return rt;
    }

    public LocalDateTime getExpiresAt() { return expiresAt; }

    public User getUser() {
        return user;
    }
}
