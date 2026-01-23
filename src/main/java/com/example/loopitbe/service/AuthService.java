package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.KakaoUserCreateRequest;
import com.example.loopitbe.dto.response.KakaoLoginResponse;
import com.example.loopitbe.dto.response.KakaoUserResponse;
import com.example.loopitbe.entity.RefreshToken;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.jwt.CookieUtil;
import com.example.loopitbe.jwt.JwtProvider;
import com.example.loopitbe.repository.RefreshTokenRepository;
import com.example.loopitbe.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final KakaoOAuthService kakaoOAuthService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(
            UserRepository userRepository,
            KakaoOAuthService kakaoOAuthService,
            JwtProvider jwtProvider,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.userRepository = userRepository;
        this.kakaoOAuthService = kakaoOAuthService;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public KakaoLoginResponse loginWithKakao(
            String accessCode,
            HttpServletResponse response
    ){
        String oAuthAccessToken = kakaoOAuthService.getToken(accessCode);
        String kakaoId = kakaoOAuthService.getKakaoId(oAuthAccessToken);

        User user = userRepository.findByKakaoId(kakaoId).orElse(null);

        // 미가입자 → JWT 발급 X
        if (user == null) {
            return new KakaoLoginResponse(kakaoId, false);
        }

        // 가입자 → JWT 발급
        issueJwt(user, response);

        return new KakaoLoginResponse(kakaoId, true);
    }

    // jwt 발급 메서드
    private void issueJwt(User user, HttpServletResponse response) {
        String accessToken = jwtProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());

        refreshTokenRepository.save(
                RefreshToken.create(
                        user,
                        refreshToken,
                        LocalDateTime.now().plusDays(7)
                )
        );

        // Access Token → Header
        response.setHeader("Authorization", "Bearer " + accessToken);

        // Refresh Token → HttpOnly Cookie
        response.addHeader(
                "Set-Cookie",
                CookieUtil.createRefreshToken(
                        refreshToken,
                        60 * 60 * 24 * 7
                ).toString()
        );
    }

    // 토큰 재발급
    public void refresh(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = extractRefreshToken(request);

        RefreshToken savedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (savedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        User user = savedToken.getUser();

        // 기존 refresh token 제거
        refreshTokenRepository.delete(savedToken);

        // 새 토큰 발급
        String newAccessToken = jwtProvider.createAccessToken(user.getUserId());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getUserId());

        refreshTokenRepository.save(
                RefreshToken.create(
                        user,
                        newRefreshToken,
                        LocalDateTime.now().plusDays(7)
                )
        );

        // Access Token → Header
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        // Refresh Token → Cookie
        response.addHeader(
                "Set-Cookie",
                CookieUtil.createRefreshToken(
                        newRefreshToken,
                        60 * 60 * 24 * 7
                ).toString()
        );
    }

    // refresh token 추출
    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    // 로그아웃
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = extractRefreshToken(request);

        // DB에 저장된 Refresh Token 삭제
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);

        // Refresh Token 쿠키 만료
        response.addHeader(
                "Set-Cookie",
                CookieUtil.deleteRefreshToken().toString()
        );
    }
}
