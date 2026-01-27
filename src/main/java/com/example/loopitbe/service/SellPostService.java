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

    /**
     * 중고 판매 게시글 생성
     */
    @Transactional
    public SellPostResponse createPost(Long userId, SellPostRequest requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        SellPost sellPost = SellPost.createPost(user, requestDto);
        SellPost savedPost = sellPostRepository.save(sellPost);

        return SellPostResponse.from(savedPost);
    }
}