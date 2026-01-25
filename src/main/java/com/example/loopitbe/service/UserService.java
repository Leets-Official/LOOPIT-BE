package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.KakaoUserCreateRequest;
import com.example.loopitbe.dto.request.UserUpdateRequest;
import com.example.loopitbe.dto.response.KakaoUserResponse;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;

import javax.xml.stream.events.DTD;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public KakaoUserResponse createKakaoUser(KakaoUserCreateRequest dto) {
        if (userRepository.existsUserByKakaoId(dto.getKakaoId()))
            throw new CustomException(ErrorCode.DUPLICATED_KAKAO_ID);

        if (userRepository.existsUserByNickname(dto.getNickname()))
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);

        if (userRepository.existsUserByEmail(dto.getEmail()))
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);

        User newUser = User.createKakaoUser(dto);

        return KakaoUserResponse.toDTO(userRepository.save(newUser));
    }

    public KakaoUserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return KakaoUserResponse.toDTO(user);
    }

    @Transactional
    public KakaoUserResponse updateUser(Long userId, UserUpdateRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getNickname().equals(dto.getNickname()) && userRepository.existsUserByNickname(dto.getNickname()))
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);

        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsUserByEmail(dto.getEmail()))
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);

        user.updateUser(dto);

        return KakaoUserResponse.toDTO(user);
    }
}
