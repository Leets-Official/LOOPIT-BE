package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.User;

import java.time.LocalDateTime;

public class KakaoUserResponse {
    private String nickname;
    private String kakaoId;
    private LocalDateTime createdAt;

    public static KakaoUserResponse toDTO(User user){
        KakaoUserResponse dto = new KakaoUserResponse();

        dto.nickname = user.getNickname();
        dto.kakaoId = user.getKakaoId();
        dto.createdAt = user.getCreatedAt();

        return dto;
    }

    public String getNickname() {
        return nickname;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
