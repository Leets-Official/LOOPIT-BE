package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.User;

import java.time.LocalDateTime;

public class KakaoUserResponse {
    private String nickname;
    private String kakaoId;
    private String name;
    private String email;
    private String profileImage;
    private LocalDateTime createdAt;

    public static KakaoUserResponse toDTO(User user){
        KakaoUserResponse dto = new KakaoUserResponse();

        dto.nickname = user.getNickname();
        dto.kakaoId = user.getKakaoId();
        dto.name = user.getName();
        dto.email = user.getEmail();
        dto.profileImage = user.getProfileImage();
        dto.createdAt = user.getCreatedAt();

        return dto;
    }

    public String getNickname() {
        return nickname;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
