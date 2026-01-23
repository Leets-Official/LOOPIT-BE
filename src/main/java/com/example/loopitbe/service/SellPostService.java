package com.example.loopitbe.service;

import com.example.loopitbe.dto.response.SellPostResponse;
import com.example.loopitbe.dto.request.SellPostRequest;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;

@Service
public class SellPostService {

    private final SellPostRepository sellPostRepository;
    private final UserRepository userRepository;

    public SellPostService(SellPostRepository sellPostRepository, UserRepository userRepository) {
        this.sellPostRepository = sellPostRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SellPostResponse createPost(String kakaoId, SellPostRequest requestDto) {
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        SellPost sellPost = SellPost.createPost(user, requestDto);
        SellPost savedPost = sellPostRepository.save(sellPost);

        return SellPostResponse.from(savedPost);
    }
}