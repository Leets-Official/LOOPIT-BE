package com.example.loopitbe.controller;

import com.example.loopitbe.dto.request.UserCreateRequest;
import com.example.loopitbe.dto.response.KakaoLoginResponse;
import com.example.loopitbe.dto.response.UserResponse;
import com.example.loopitbe.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest dto){
        return service.createUser(dto);
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(@RequestParam("code") String accessCode) {
        KakaoLoginResponse response = service.loginWithKakao(accessCode);

        if (response.getRegistered()){

        }

        return ResponseEntity.ok(response);
    }
}
