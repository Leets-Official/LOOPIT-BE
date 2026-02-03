package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.KakaoUserCreateRequest;
import com.example.loopitbe.dto.response.KakaoLoginResponse;
import com.example.loopitbe.dto.response.KakaoUserResponse;
import com.example.loopitbe.service.AuthService;
import com.example.loopitbe.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "카카오 OAuth 로그인 및 회원가입")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;
    private final UserService userService;

    public AuthController(AuthService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 로그인 성공 시 JWT 인증처리, registered 필드로 서비스 가입 여부 return"
    )
    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse<KakaoLoginResponse>> kakaoLogin(
            @RequestParam("code") String accessCode,
            HttpServletResponse response
    ) {
        KakaoLoginResponse result = service.loginWithKakao(accessCode, response);

        return ResponseEntity.ok(ApiResponse.ok(result, "카카오 로그인 성공."));
    }

    @Operation(
            summary = "신규 회원 생성",
            description = "회원가입 성공 시 회원 정보 return"
    )
    @PostMapping("/register/kakao")
    public ResponseEntity<ApiResponse<KakaoUserResponse>> createKakaoUser(
            @Valid @RequestBody KakaoUserCreateRequest dto,
            HttpServletResponse response){
        KakaoUserResponse result = userService.createKakaoUser(dto, response);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(result, "카카오 유저 회원가입 성공."));
    }

    @Operation(
            summary = "refresh token 재발급",
            description = "refresh token을 검증한 후 새로운 access token을 재발급하며, 필요 시 refresh token도 함께 갱신"
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Object>> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        service.refresh(request, response);

        return ResponseEntity.ok(
                ApiResponse.ok(null, "토큰 재발급 성공.")
        );
    }

    @Operation(
            summary = "로그아웃",
            description = "Refresh Token을 삭제하고 쿠키를 만료시켜 로그아웃 처리"
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        service.logout(request, response);

        return ResponseEntity.ok(
                ApiResponse.ok(null, "로그아웃 성공.")
        );
    }
}
