package com.example.loopitbe.dto.response;

public class KakaoLoginResponse {
    private Long userId;
    private Boolean registered;    // 로그인 응답 or 회원가입 응답
    private String kakaoId;

    public KakaoLoginResponse() {
    }

    public KakaoLoginResponse(Long userId, String kakaoId, Boolean registered) {
        this.userId = userId;
        this.kakaoId = kakaoId;
        this.registered = registered;
    }

    public Long getUserId() { return userId;}

    public Boolean getRegistered() {
        return registered;
    }

    public String getKakaoId() {
        return kakaoId;
    }
}
