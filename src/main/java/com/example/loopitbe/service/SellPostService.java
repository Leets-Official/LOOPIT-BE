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
import java.util.List;

@Service
public class SellPostService {

    private final SellPostRepository sellPostRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public SellPostService(SellPostRepository sellPostRepository, UserRepository userRepository, S3Service s3Service) {
        this.sellPostRepository = sellPostRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
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

    /**
     * 중고 판매 게시글 수정 (이미지 변경 시 기존 이미지 삭제 포함)
     */
    @Transactional
    public void updatePost(Long postId, SellPostRequest request) {
        SellPost post = sellPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.SELL_POST_NOT_FOUND));

        // 1. 기존 이미지들이 S3에 있다면 모두 삭제
        List<String> oldUrls = post.getImageUrls();
        if (oldUrls != null && !oldUrls.isEmpty()) {
            for (String oldUrl : oldUrls) {
                s3Service.deleteFile(oldUrl);
            }
        }

        // 2. 엔티티 정보 업데이트 (새로운 이미지 URL 리스트 포함)
        post.update(request);
    }
}