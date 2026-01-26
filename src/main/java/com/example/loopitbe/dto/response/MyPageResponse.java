package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.User;

public record MyPageResponse(
        String nickname,
        String email,
        String profileImageUrl,
        long sellCount,
        long buyCount,
        long wishCount
) {
    // 정적 팩토리 메서드: 엔티티와 외부 데이터(S3 URL, 집계 수치)를 결합하여 생성
    public static MyPageResponse of(User user, String profileImageUrl, long sellCount, long buyCount, long wishCount) {
        return new MyPageResponse(
                user.getNickname(),
                user.getEmail(),
                profileImageUrl,
                sellCount,
                buyCount,
                wishCount
        );
    }
}