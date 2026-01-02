package com.example.loopitbe.dto.response;

public class KakaoLoginResponse {
    private Boolean registered;    // 로그인 응답 or 회원가입 응답
    private String kakaoId;

    public KakaoLoginResponse() {
    }

    public KakaoLoginResponse(String kakaoId, Boolean registered) {
        this.kakaoId = kakaoId;
        this.registered = registered;
    }

    public Boolean getRegistered() {
        return registered;
    }

    public String getKakaoId() {
        return kakaoId;
    }
}
