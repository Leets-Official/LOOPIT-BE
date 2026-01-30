package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.SellPostRequest;
import com.example.loopitbe.dto.request.SellPostSearchCondition;
import com.example.loopitbe.dto.response.SellPostDetailResponse;
import com.example.loopitbe.dto.response.SellPostListResponse;
import com.example.loopitbe.dto.response.SellPostResponse;
import com.example.loopitbe.dto.response.UserSellPostResponse;
import com.example.loopitbe.service.SellPostService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/sell-post")
public class SellPostController {

    private final SellPostService sellPostService;

    public SellPostController(SellPostService sellPostService) {
        this.sellPostService = sellPostService;
    }

    // 판매글 등록
    @Operation(
            summary = "판매글 등록",
            description = "판매글 등록 후 기본 정보 return"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<SellPostResponse>> createSellPost(
            @RequestBody SellPostRequest requestDto,
            @RequestAttribute Long userId) {

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
            description = "판매 상태, 제조사, 시리즈, 가격대 필터를 적용하여 판매글 목록을 10개씩 페이징 조회"
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
            @PathVariable Long postId
    ) {
        SellPostDetailResponse response = sellPostService.getSellPostDetail(postId);

        return ResponseEntity.ok(ApiResponse.ok(response, "판매글 상세 조회 성공."));
    }

    @Operation(summary = "판매글 수정", description = "기존 판매글의 내용을 수정. 삭제된 이미지는 S3에서도 제거.")
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<SellPostResponse>> updateSellPost(
            @PathVariable Long postId,
            @RequestBody SellPostRequest requestDto,
            @RequestAttribute Long userId
    ) {
        SellPostResponse response = sellPostService.updatePost(postId, userId, requestDto);
        return ResponseEntity.ok(ApiResponse.ok(response, "판매글 수정 성공."));
    }

    @Operation(summary = "판매글 삭제", description = "판매글을 삭제하며, S3에 저장된 이미지도 함께 삭제.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deleteSellPost(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        sellPostService.deletePost(postId, userId);
        return ResponseEntity.ok(ApiResponse.ok(null, "판매글 삭제 성공."));
    }
}