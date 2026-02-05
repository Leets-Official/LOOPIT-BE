package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.UserImgUpdateRequest;
import com.example.loopitbe.dto.request.UserUpdateRequest;
import com.example.loopitbe.dto.response.KakaoUserResponse;
import com.example.loopitbe.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 컨트롤러(회원가입, 조회 등)")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(
            summary = "사용자 정보 조회",
            description = "userId에 해당하는 사용자 정보 return, 없으면 404"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<KakaoUserResponse>> getUser(@Parameter(hidden = true) @AuthenticationPrincipal Long userId){
        return ResponseEntity.ok(ApiResponse.ok(service.getUser(userId), "사용자 정보 조회 성공."));
    }

    @Operation(
            summary = "사용자 정보 업데이트",
            description = "nickname, name, email, birthdate 필드 업데이트. 모든 필드는 null 또는 blank불가."
    )
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<KakaoUserResponse>> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.updateUser(userId, request), "사용자 정보 업데이트 완료."));
    }

    @PutMapping("/image/{userId}")
    public ResponseEntity<ApiResponse<KakaoUserResponse>> updateUserProfileImg(
            @PathVariable Long userId,
            @Valid @RequestBody UserImgUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.updateUserProfileImg(userId, request.getImgUrl()), "사용자 프로필 이미지 업데이트 완료."));
    }
}
