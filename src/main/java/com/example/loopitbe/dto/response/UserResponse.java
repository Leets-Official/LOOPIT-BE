package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.User;

import java.time.LocalDateTime;

public class UserResponse {
    private String nickname;
    private String kakaoId;
    private LocalDateTime createdAt;

    public static UserResponse toDTO(User user){
        UserResponse dto = new UserResponse();

        dto.nickname = user.getNickname();
        dto.kakaoId = user.getKakaoId();
        dto.createdAt = user.getCreatedAt();

        return dto;
    }
}
