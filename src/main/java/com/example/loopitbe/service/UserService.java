package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.KakaoUserCreateRequest;
import com.example.loopitbe.dto.request.UserUpdateRequest;
import com.example.loopitbe.dto.response.KakaoUserResponse;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;

import javax.xml.stream.events.DTD;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final AuthService authService;

    public UserService(UserRepository userRepository, S3Service s3Service, AuthService authService) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
        this.authService = authService;
    }

    public KakaoUserResponse createKakaoUser(KakaoUserCreateRequest dto, HttpServletResponse response) {
        if (userRepository.existsUserByKakaoId(dto.getKakaoId()))
            throw new CustomException(ErrorCode.DUPLICATED_KAKAO_ID);

        if (userRepository.existsUserByNickname(dto.getNickname()))
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);

        if (userRepository.existsUserByEmail(dto.getEmail()))
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);

        User newUser = User.createKakaoUser(dto);

        authService.issueJwt(newUser, response);

        return KakaoUserResponse.from(userRepository.save(newUser));
    }

    public KakaoUserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return KakaoUserResponse.from(user);
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

        return KakaoUserResponse.from(user);
    }

    @Transactional
    public KakaoUserResponse updateUserProfileImg(Long userId, String imgUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 요청 이미지경로 null 혹은 공백이면 이미지 삭제
        if (imgUrl == null || imgUrl.isEmpty()){
            s3Service.deleteImage(imgUrl);
            user.updateProfileImage(null);  // db에 null값 삽입
        }
        else{
            user.updateProfileImage(imgUrl);
        }

        return KakaoUserResponse.from(user);
    }
}