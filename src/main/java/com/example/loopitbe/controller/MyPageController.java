package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.response.MyPageResponse;
import com.example.loopitbe.dto.response.TransactionHistoryResponse;
import com.example.loopitbe.service.MyPageService;
import com.example.loopitbe.service.TransactionService;
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
    private final TransactionService transactionService;

    public MyPageController(MyPageService myPageService, TransactionService transactionService) {
        this.myPageService = myPageService;
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "마이페이지 메인 정보 조회",
            description = "마이페이지 최초 로드 시 정보 로드. 사용자 대시보드 정보 및 전체 구매내역 반환"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<MyPageResponse>> getMyPageMain(@RequestParam Long userId) {
        MyPageResponse response = myPageService.getMyPageInfo(userId);
        return ResponseEntity.ok(ApiResponse.ok(response, "마이페이지 메인 정보 조회 성공."));
    }

    @Operation(
            summary = "구매 내역 조회",
            description = "status에 RESERVED(예약중) 혹은 COMPLETED(판매완료) 값으로 요청. (값 지정 안할 시 ALL)"
    )
    @GetMapping("/history/buy")
    public ResponseEntity<ApiResponse<List<TransactionHistoryResponse>>> getBuyHistory(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "ALL") String status) {
        List<TransactionHistoryResponse> responses = transactionService.getBuyHistory(userId, status);
        return ResponseEntity.ok(ApiResponse.ok(responses, "구매 내역 조회 성공."));
    }

    @Operation(
            summary = "판매 내역 조회",
            description = "status에 RESERVED(예약중) 혹은 COMPLETED(판매완료) 값으로 요청. (값 지정 안할 시 ALL)"
    )
    @GetMapping("/history/sell")
    public ResponseEntity<ApiResponse<List<TransactionHistoryResponse>>> getSellHistory(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "ALL") String status) {
        List<TransactionHistoryResponse> responses = transactionService.getSellHistory(userId, status);
        return ResponseEntity.ok(ApiResponse.ok(responses, "판매 내역 조회 성공."));
    }
}