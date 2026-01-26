package com.example.loopitbe.controller;

// 1. 패키지 경로 수정: loopit -> loopitbe
import com.example.loopitbe.dto.response.MyPageResponse;
import com.example.loopitbe.dto.request.ProfileUpdateRequest;
import com.example.loopitbe.dto.response.TradeHistoryResponse;
import com.example.loopitbe.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MyPage", description = "마이페이지 및 계정 관리 API")
@RestController
@RequestMapping("/api/v1/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @Operation(summary = "마이페이지 메인 정보 조회")
    @GetMapping("/{kakaoId}") // 2. @AuthenticationPrincipal 대신 경로 변수 또는 인증 객체에서 kakaoId 추출
    public ResponseEntity<MyPageResponse> getMyPageMain(@PathVariable String kakaoId) {
        // Long userId -> String kakaoId로 변경하여 서비스 호출
        MyPageResponse response = myPageService.getMyPageInfo(kakaoId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "구매/판매 내역 조회")
    @GetMapping("/{kakaoId}/transactions")
    public ResponseEntity<List<TradeHistoryResponse>> getTradeHistory(
            @PathVariable String kakaoId,
            @RequestParam String type,
            @RequestParam(defaultValue = "ALL") String status) {
        List<TradeHistoryResponse> responses = myPageService.getTradeHistory(kakaoId, type, status);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "개인정보 수정")
    @PatchMapping("/{kakaoId}/users/me")
    public ResponseEntity<Void> updateProfile(
            @PathVariable String kakaoId,
            @RequestBody ProfileUpdateRequest request) {
        myPageService.updateProfile(kakaoId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로필 이미지 변경용 URL 발급")
    @PostMapping("/{kakaoId}/users/me/image")
    public ResponseEntity<String> getProfileImageUploadUrl(
            @PathVariable String kakaoId,
            @RequestParam String fileName) {
        String uploadUrl = myPageService.getUploadUrl(kakaoId, fileName);
        return ResponseEntity.ok(uploadUrl);
    }
}