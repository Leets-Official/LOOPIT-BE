package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.KakaoUserCreateRequest;
import com.example.loopitbe.dto.response.KakaoLoginResponse;
import com.example.loopitbe.dto.response.UserResponse;
import com.example.loopitbe.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "카카오 OAuth 로그인 및 회원가입")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(
            summary = "신규 회원 생성",
            description = "회원가입 성공 시 회원 정보 return"
    )
    @PostMapping("/register/kakao")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody KakaoUserCreateRequest dto){
        UserResponse response = service.createKakaoUser(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "회원가입 성공."));
    }

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 로그인 성공 시 JWT 인증처리, registered 필드로 서비스 가입 여부 return"
    )
    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse<KakaoLoginResponse>> kakaoLogin(@RequestParam("code") String accessCode) {
        KakaoLoginResponse response = service.loginWithKakao(accessCode);

        // 가입된 카카오 아이디 인 경우
        if (response.getRegistered()){
            // jwt 인증 처리
        }

        return ResponseEntity.ok(ApiResponse.ok(response, "카카오 로그인 성공."));
    }
}
