package com.example.loopitbe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCreateRequest {
    // 회원 가입 시 입력 정보는 후에 와이어 프레임 나오면 리펙토링
    private String kakaoId;
    @NotBlank
    @Size(max = 10)
    private String nickname;

    public String getKakaoId() {
        return kakaoId;
    }

    public String getNickname() {
        return nickname;
    }
}
