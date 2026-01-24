package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.WishlistToggleRequest;
import com.example.loopitbe.service.WishListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WishList", description = "사용자 관련 컨트롤러(회원가입, 조회 등)")
@RestController
@RequestMapping("/wishlist")
public class WishListController {
    private final WishListService service;

    public WishListController(WishListService service) {
        this.service = service;
    }

    @PostMapping("/toggle")
    public ResponseEntity<ApiResponse<String>> toggle(@Valid @RequestBody WishlistToggleRequest dto) {
        service.toggleWishList(dto);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.ok("", "찜 상태 토글 완료."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WishListSellPostResponse>>> getMyWishList(@RequestParam("userId") Long userId) {

    }
}
