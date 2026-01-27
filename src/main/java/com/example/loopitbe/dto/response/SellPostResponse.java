package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.SellPost;
import java.time.LocalDateTime;
import java.util.List;

public record SellPostResponse(
        Long postId,
        String title,
        String content,
        Long price,
        String model,
        String manufacturer,
        String color,
        String capacity,

        // Enum 및 상세 조건 필드
        String status,           // "판매중", "예약중" 등
        String batteryStatus,    // "80% 이상" 등
        boolean isUsed,          // 중고 여부
        boolean hasScratch,      // 스크래치 여부
        boolean isScreenCracked, // 화면 파손 여부

        List<String> imageUrls,
        LocalDateTime createdAt
) {
    /**
     * SellPost 엔티티를 SellPostResponse DTO로 변환
     */
    public static SellPostResponse from(SellPost post) {
        return new SellPostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.getModel(),
                post.getManufacturer(),
                post.getColor(),
                post.getCapacity(),

                post.getStatus().getDescription(),
                post.getBatteryStatus().getDescription(),

                post.isUsed(),
                post.isHasScratch(),
                post.isScreenCracked(),

                post.getImageUrls(),
                post.getCreatedAt()
        );
    }
}