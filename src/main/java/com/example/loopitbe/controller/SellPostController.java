package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.SellPostRequest;
import com.example.loopitbe.dto.SellPostResponse;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.service.S3Service;
import com.example.loopitbe.service.SellPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/sell-posts")
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

        SellPost savedPost = sellPostService.createPost(principal.getName(), requestDto);

        SellPostResponse responseData = new SellPostResponse(
                savedPost.getId(),
                savedPost.getTitle(),
                "판매글이 성공적으로 등록되었습니다.",
                LocalDateTime.now()
        );

        return ResponseEntity.ok(ApiResponse.ok(responseData, "판매글 등록 완료"));
    }
}