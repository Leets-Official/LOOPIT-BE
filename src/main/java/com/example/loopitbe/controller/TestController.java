package com.example.loopitbe.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/auth")
    public String authTest(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return "인증된 사용자 ID: " + userId;
    }
}
