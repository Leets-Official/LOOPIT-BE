package com.example.loopitbe.entity;

import com.example.loopitbe.dto.request.UserCreateRequest;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // DB 테이블명
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "user_id")
    private Long userId;

    @Column(length = 20, unique = true)
    private String nickname;

    @Column(name = "login_method", nullable = false)
    private String loginMethod;

    @Column(name = "kakao_id", unique = true)
    private String kakaoId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 기본 생성자
    protected User() {}

    // 카카오 & 이메일 회원가입
    public static User createUser(UserCreateRequest dto){
        User user = new User();
        user.kakaoId =dto.getKakaoId();
        user.nickname = dto.getNickname();
        user.createdAt = LocalDateTime.now();
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public String getLoginMethod() {
        return loginMethod;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getUserId() {
        return userId;
    }
}
