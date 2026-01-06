package com.example.loopitbe.jwt;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static ResponseCookie createRefreshToken(
            String value,
            long maxAge
    ) {
        return ResponseCookie.from("refreshToken", value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("None")
                .build();
    }
}
