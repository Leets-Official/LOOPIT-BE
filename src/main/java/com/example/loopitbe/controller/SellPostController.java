package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.SellPostRequest;
import com.example.loopitbe.dto.response.SellPostResponse;
import com.example.loopitbe.service.SellPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SellPost", description = "판매 게시글 관련 API")
@RestController
@RequestMapping("/sell-posts")
public class SellPostController {

    private final SellPostService sellPostService;

    public SellPostController(SellPostService sellPostService) {
        this.sellPostService = sellPostService;
    }


    @Operation(summary = "판매 게시글 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<SellPostResponse>> createSellPost(
            @RequestParam Long userId,
            @RequestBody SellPostRequest requestDto) {

        SellPostResponse responseData = sellPostService.createPost(userId, requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(responseData, "판매글이 성공적으로 등록되었습니다."));
    }
}