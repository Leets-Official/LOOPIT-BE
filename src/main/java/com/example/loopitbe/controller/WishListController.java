package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.WishlistToggleRequest;
import com.example.loopitbe.dto.response.WishListSellPostResponse;
import com.example.loopitbe.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "WishList", description = "찜 토글, 찜 목록 조회 기능 컨트롤러")
@RestController
@RequestMapping("/wishlist")
public class WishListController {
    private final WishListService service;

    public WishListController(WishListService service) {
        this.service = service;
    }

    @Operation(
            summary = "찜 토글",
            description = "찜 안돼있으면 wishlists테이블에 추가(Enabled 반환), 있으면 삭제처리(Disabled 반환). "
    )
    @PostMapping("/toggle")
    public ResponseEntity<ApiResponse<String>> toggle(@Valid @RequestBody WishlistToggleRequest dto) {
        String status = service.toggleWishList(dto);

        return ResponseEntity.ok(ApiResponse.ok(status, "찜상태 토글 완료."));
    }

    @Operation(
            summary = "찜 목록 조회",
            description = "userId에 해당하는 사용자가 찜한 모든 찜 목록 조회"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<WishListSellPostResponse>>> getMyWishList(@RequestParam("userId") Long userId) {
        List<WishListSellPostResponse> responses = service.getMyWishLists(userId);

        return ResponseEntity.ok(ApiResponse.ok(responses, "나의 찜목록 조회 성공."));
    }
}
