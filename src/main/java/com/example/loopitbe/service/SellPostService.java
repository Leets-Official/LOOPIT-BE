package com.example.loopitbe.service;

import com.example.loopitbe.dto.SellPostRequest;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellPostService {

    private final SellPostRepository sellPostRepository;
    private final UserRepository userRepository;

    public SellPostService(SellPostRepository sellPostRepository, UserRepository userRepository) {
        this.sellPostRepository = sellPostRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SellPost createPost(String kakaoId, SellPostRequest requestDto) {

        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        SellPost sellPost = SellPost.createPost(user, requestDto);

        return sellPostRepository.save(sellPost);
    }
}