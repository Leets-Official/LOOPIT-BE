package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.*;
import com.example.loopitbe.dto.response.*;
import com.example.loopitbe.service.SellPostService;
import com.example.loopitbe.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sell-post")
public class SellPostController {

    private final SellPostService sellPostService;
    private final TransactionService transactionService;

    public SellPostController(SellPostService sellPostService, TransactionService transactionService) {
        this.sellPostService = sellPostService;
        this.transactionService = transactionService;
    }

    // 판매글 등록
    @Operation(
            summary = "판매글 등록",
            description = "판매글 등록 후 기본 정보 return"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<SellPostResponse>> createSellPost(
            @RequestBody SellPostRequest requestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId) {

        SellPostResponse response = sellPostService.createPost(userId, requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "판매글 등록 성공."));
    }

    @Operation(
            summary = "사용자의 판매글 조회",
            description = "userId에 해당하는 판매글 리스트 return. 10개씩 페이지네이션 적용."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Page<UserSellPostResponse>>> getSellPostByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(ApiResponse.ok(sellPostService.getSellPostByUser(userId, page), "사용자 판매글 조회 성공."));
    }
    // 판매글 목록 조회
    @Operation(
            summary = "판매글 목록 조회",
            description = "제조사, 시리즈, 가격대, 키워드 필터를 적용하여 판매글 목록을 10개씩 페이징 조회"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SellPostListResponse>>> getSellPosts(
            @RequestParam(defaultValue = "0") int page,
            @ModelAttribute SellPostSearchCondition condition
    ) {
        Page<SellPostListResponse> response = sellPostService.getSellPosts(page, condition);

        return ResponseEntity.ok(ApiResponse.ok(response, "판매글 목록 조회 성공."));
    }

    // 판매글 상세 조회
    @Operation(
            summary = "판매글 상세 조회",
            description = "특정 판매글의 상세 정보 및 동일한 시리즈 상품을 최대 4개 조회"
    )
    @GetMapping("/detail/{postId}")
    public ResponseEntity<ApiResponse<SellPostDetailResponse>> getSellPostDetail(
            @PathVariable Long postId,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    ) {
        SellPostDetailResponse response = sellPostService.getSellPostDetail(postId, userId);

        return ResponseEntity.ok(ApiResponse.ok(response, "판매글 상세 조회 성공."));
    }

    @Operation(summary = "판매글 수정", description = "기존 판매글의 내용을 수정. 삭제된 이미지는 S3에서도 제거.")
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<SellPostResponse>> updateSellPost(
            @PathVariable Long postId,
            @RequestBody SellPostRequest requestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal Object principal
    ) {
        Long userId = null;
        if (principal instanceof Long) {
            userId = (Long) principal;
        }

        SellPostResponse response = sellPostService.updatePost(postId, userId, requestDto);
        return ResponseEntity.ok(ApiResponse.ok(response, "판매글 수정 성공."));
    }

    @Operation(summary = "판매글 삭제", description = "판매글을 삭제하며, S3에 저장된 이미지도 함께 삭제.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deleteSellPost(
            @PathVariable Long postId,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    ) {
        sellPostService.deletePost(postId, userId);
        return ResponseEntity.ok(ApiResponse.ok(null, "판매글 삭제 성공."));
    }

    // 판매 상품 예약하기
    @Operation(
            summary = "판매 상품 예약하기",
            description = "판매 상품 예약. SellPost, Transaction의 status 필드를 예약중으로 변경. 이미 진행중 혹은 완료된 거래 존재 시 에러"
    )
    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse<TransactionHistoryResponse>> createTransaction(@RequestBody CreateTransactionRequest request, @Parameter(hidden = true) @AuthenticationPrincipal Long userId) {

        TransactionHistoryResponse response = transactionService.createTransaction(request, userId);

        return ResponseEntity.ok(ApiResponse.ok(response, "판매 상품 예약 성공."));
    }

    // 판매글 등록
    @Operation(
            summary = "판매 상품 거래 완료",
            description = "판매 상품 거래 완료하기. SellPost, Transaction의 status 필드를 거래완료로 변경. 이미 완료된 거래일 시 에러, 판매중인 게시물인 경우 거래를 새로 만들어서 저장"
    )
    @PostMapping("/complete")
    public ResponseEntity<ApiResponse<TransactionHistoryResponse>> completeTransaction(@RequestBody CompleteTransactionRequest request, @Parameter(hidden = true) @AuthenticationPrincipal Long userId) {

        TransactionHistoryResponse response = transactionService.completeTransaction(request, userId);

        return ResponseEntity.ok(ApiResponse.ok(response, "판매 상품 거래 완료."));
    }

    // 판매글 등록
    @Operation(
            summary = "판매 상품 다시 판매중으로 설정",
            description = "예약중인 판매 상품을 다시 판매중으로 설정. SellPost, Transaction의 status 필드를 취소됨으로 변경. 완료된 혹은 예약 중인 거래 없을 시 에러"
    )
    @PostMapping("/active")
    public ResponseEntity<ApiResponse<TransactionHistoryResponse>> cancelTransaction(@RequestBody CancelTransactionRequest request, @Parameter(hidden = true) @AuthenticationPrincipal Long userId) {

        TransactionHistoryResponse response = transactionService.cancelTransaction(request, userId);

        return ResponseEntity.ok(ApiResponse.ok(response, "판매 상품 판매중으로 변경 완료."));
    }
}