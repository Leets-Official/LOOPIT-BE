package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.UserCreateRequest;
import com.example.loopitbe.dto.response.KakaoLoginResponse;
import com.example.loopitbe.dto.response.UserResponse;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final KakaoOAuthService kakaoOAuthService;

    public AuthService(UserRepository userRepository, KakaoOAuthService kakaoOAuthService) {
        this.userRepository = userRepository;
        this.kakaoOAuthService = kakaoOAuthService;
    }

    public UserResponse createUser(UserCreateRequest dto) {
        if (userRepository.existsUserByKakaoId(dto.getKakaoId()))
                throw new CustomException(ErrorCode.DUPLICATED_KAKAO_ID);

        if (userRepository.existsUserByNickname(dto.getNickname()))
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);

        User newUser = User.createUser(dto);

        return UserResponse.toDTO(userRepository.save(newUser));
    }

    public KakaoLoginResponse loginWithKakao(String accessCode){
        String oAuthAccessToken = kakaoOAuthService.getToken(accessCode);
        String kakaoId = kakaoOAuthService.getKakaoId(oAuthAccessToken);

        User user = userRepository.findByKakaoId(kakaoId).orElse(null);

        return user == null ?
                new KakaoLoginResponse(kakaoId, false) : new KakaoLoginResponse(kakaoId, true);
    }
}
