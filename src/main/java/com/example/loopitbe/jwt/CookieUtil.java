package com.example.loopitbe.jwt;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static ResponseCookie create(
            String name,
            String value,
            long maxAge
    ) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("None")
                .build();
    }
}
