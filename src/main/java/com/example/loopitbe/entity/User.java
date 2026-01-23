package com.example.loopitbe.entity;

import com.example.loopitbe.dto.request.KakaoUserCreateRequest;
import com.example.loopitbe.dto.request.UserUpdateRequest;
import com.example.loopitbe.enums.LoginProvider;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // DB 테이블명
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "user_id")
    private Long userId;

    @Column(length = 20, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "login_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginProvider loginMethod;

    @Column(name = "kakao_id", unique = true)
    private String kakaoId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 기본 생성자
    protected User() {}

    // 카카오  회원가입
    public static User createKakaoUser(KakaoUserCreateRequest dto) {
        User user = new User();
        user.loginMethod = LoginProvider.KAKAO;
        user.kakaoId = dto.getKakaoId();
        user.nickname = dto.getNickname();
        user.name = dto.getName();
        user.email = dto.getEmail();
        user.birthdate = dto.getBirthdate();
        user.profileImage = dto.getProfileImage();
        user.createdAt = LocalDateTime.now();
        return user;
    }

    public void updateUser(UserUpdateRequest dto) {
        this.nickname = dto.getNickname();
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.birthdate = dto.getBirthdate();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getKakaoId() { return kakaoId; }

    public String getEmail() { return email; }

    public LoginProvider getLoginMethod() {
        return loginMethod;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
