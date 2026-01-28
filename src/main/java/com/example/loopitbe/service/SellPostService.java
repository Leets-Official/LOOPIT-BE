package com.example.loopitbe.service;

import com.example.loopitbe.dto.response.SellPostResponse;
import com.example.loopitbe.dto.request.SellPostRequest;
import com.example.loopitbe.dto.response.UserSellPostResponse;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;

import java.util.List;

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

    public Page<UserSellPostResponse> getSellPostByUser(Long userId, int page){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<SellPost> posts = sellPostRepository.findAllByUser_UserId(userId, pageable);

        return posts.map(UserSellPostResponse::from);
    }
}