package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.IsShopInWishListRequest;
import com.example.loopitbe.dto.request.PostWishlistToggleRequest;
import com.example.loopitbe.dto.request.ShopWishlistToggleRequest;
import com.example.loopitbe.dto.response.IsShopInWishListResponse;
import com.example.loopitbe.dto.response.SellPostWishListResponse;
import com.example.loopitbe.dto.response.ShopWishListResponse;
import com.example.loopitbe.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
            summary = "판매 게시글 찜 토글",
            description = "찜 안돼있으면 post_wishlists테이블에 추가(Enabled 반환), 있으면 삭제처리(Disabled 반환). "
    )
    @PostMapping("/post/toggle")
    public ResponseEntity<ApiResponse<String>> togglePost(@Valid @RequestBody PostWishlistToggleRequest dto) {
        String status = service.togglePostWishList(dto);

        return ResponseEntity.ok(ApiResponse.ok(status, "판매글 찜상태 토글 완료."));
    }

    @Operation(
            summary = "판매 게시글찜 목록 조회",
            description = "userId에 해당하는 사용자가 찜한 모든 찜 목록 조회"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<SellPostWishListResponse>>> getMyPostWishList(@RequestParam("userId") Long userId) {
        List<SellPostWishListResponse> responses = service.getMyPostWishLists(userId);

        return ResponseEntity.ok(ApiResponse.ok(responses, "나의 판매글 찜목록 조회 성공."));
    }

    @Operation(
            summary = "수리점 찜 토글",
            description = "찜 안돼있으면 shop_wishlists테이블에 추가(Enabled 반환), 있으면 삭제처리(Disabled 반환). "
    )
    @PostMapping("/post/toggle")
    public ResponseEntity<ApiResponse<String>> toggleShop(@Valid @RequestBody ShopWishlistToggleRequest dto) {
        String status = service.toggleShopWishList(dto);

        return ResponseEntity.ok(ApiResponse.ok(status, "수리점 찜상태 토글 완료."));
    }

    @Operation(
            summary = "수리점 찜 목록 조회",
            description = "userId에 해당하는 사용자가 찜한 모든 찜 목록 조회"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<ShopWishListResponse>>> getMyShopWishList(@RequestParam("userId") Long userId) {
        List<ShopWishListResponse> responses = service.getMyShopWishLists(userId);

        return ResponseEntity.ok(ApiResponse.ok(responses, "나의 수리점 찜목록 조회 성공."));
    }

    @Operation(
            summary = "수리점이 찜 목록에 있는지 조회",
            description = "클라이언트에서 현재 위치 주변 수리점 리스트 전달 시 찜목록에 해당하는 지 조회. isShopInWishList boolean값으로 return"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<List<IsShopInWishListResponse>>> isShopInWishList(@Valid @RequestBody IsShopInWishListRequest dto) {
        List<IsShopInWishListResponse> responses = service.checkShopWishList(dto);

        return ResponseEntity.ok(ApiResponse.ok(responses, "나의 찜목록 조회 성공."));
    }
}
