package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
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
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @Operation(summary = "마이페이지 메인 정보 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<MyPageResponse>> getMyPageMain(@RequestParam Long userId) {
        MyPageResponse response = myPageService.getMyPageInfo(userId);
        return ResponseEntity.ok(ApiResponse.ok(response, "마이페이지 메인 정보 조회 성공."));
    }

    @Operation(summary = "구매/판매 내역 조회")
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<TradeHistoryResponse>>> getTradeHistory(
            @RequestParam Long userId,
            @RequestParam String type,
            @RequestParam(defaultValue = "ALL") String status) {
        List<TradeHistoryResponse> responses = myPageService.getTradeHistory(userId, type, status);
        return ResponseEntity.ok(ApiResponse.ok(responses, "거래 내역 조회 성공."));
    }

    @Operation(summary = "개인정보 수정")
    @PatchMapping("/users/me")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @RequestParam Long userId,
            @RequestBody ProfileUpdateRequest request) {
        myPageService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.ok(null, "프로필 수정이 완료되었습니다."));
    }

}