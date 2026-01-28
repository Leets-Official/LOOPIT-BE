package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.SellPostRequest;
import com.example.loopitbe.dto.response.SellPostResponse;
import com.example.loopitbe.dto.response.UserSellPostResponse;
import com.example.loopitbe.service.S3Service;
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

    private final S3Service s3Service;
    private final SellPostService sellPostService;

    public SellPostController(S3Service s3Service, SellPostService sellPostService) {
        this.s3Service = s3Service;
        this.sellPostService = sellPostService;
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<ApiResponse<String>> getPresignedUrl(@RequestParam String fileName) {
        String url = s3Service.getPresignedUrl("products", fileName);
        return ResponseEntity.ok(ApiResponse.ok(url, "Presigned URL이 생성되었습니다."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SellPostResponse>> createSellPost(
            @RequestBody SellPostRequest requestDto,
            Principal principal) {

        SellPostResponse responseData = sellPostService.createPost(principal.getName(), requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(responseData, "판매글이 성공적으로 등록되었습니다."));
    }



    @Operation(
            summary = "사용자의 판매글 조회",
            description = "userId에 해당하는 판매글 리스트 return. 10개씩 페이지네이션 적용."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Page<UserSellPostResponse>>> getSellPostByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page){
        return ResponseEntity.ok(ApiResponse.ok(sellPostService.getSellPostByUser(userId, page), "사용자 판매글 조회 성공."));
    }
}