package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.SellPostSearchCondition;
import com.example.loopitbe.dto.response.SellPostDetailResponse;
import com.example.loopitbe.dto.response.SellPostListResponse;
import com.example.loopitbe.dto.response.SellPostResponse;
import com.example.loopitbe.dto.request.SellPostRequest;
import com.example.loopitbe.dto.response.UserSellPostResponse;
import com.example.loopitbe.dto.response.SimilarPostResponse;
import com.example.loopitbe.entity.Device;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.repository.DeviceRepository;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.SellPostSpecification;
import com.example.loopitbe.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellPostService {

    private final SellPostRepository sellPostRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final S3Service s3Service;

    public SellPostService(SellPostRepository sellPostRepository, UserRepository userRepository, DeviceRepository deviceRepository, S3Service s3Service) {
        this.sellPostRepository = sellPostRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.s3Service = s3Service;
    }

    /**
     * 중고 판매 게시글 생성
     */
    @Transactional
    public SellPostResponse createPost(Long userId, SellPostRequest requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 시리즈 정보 저장
        Device device = deviceRepository.findByModel(requestDto.getModel())
                .orElseThrow(() -> new CustomException(ErrorCode.DEVICE_NOT_FOUND));

        String series = device.getSeries();

        SellPost sellPost = SellPost.createPost(user, requestDto, series);
        SellPost savedPost = sellPostRepository.save(sellPost);

        return SellPostResponse.from(savedPost);
    }

    public Page<UserSellPostResponse> getSellPostByUser(Long userId, int page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<SellPost> posts = sellPostRepository.findAllByUser_UserId(userId, pageable);

        return posts.map(UserSellPostResponse::from);
    }
    // 목록 조회
    @Transactional(readOnly = true)
    public Page<SellPostListResponse> getSellPosts(int page, SellPostSearchCondition condition) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Specification<SellPost> spec = SellPostSpecification.search(condition);

        return sellPostRepository.findAll(spec, pageable)
                .map(SellPostListResponse::from);
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public SellPostDetailResponse getSellPostDetail(Long postId) {
        // 1. 게시글 조회
        SellPost post = sellPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 2. 비슷한 상품 조회 로직
        List<SimilarPostResponse> similarPostResponses = getSimilarPosts(post);

        // 3. DTO 변환 및 반환
        return new SellPostDetailResponse(post, similarPostResponses);
    }

    // 수정
    @Transactional
    public SellPostResponse updatePost(Long postId, Long userId, SellPostRequest requestDto) {
        SellPost post = sellPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 본인 확인
        validateWriter(post, userId);

        // 이미지 비교 및 삭제 로직
        List<String> oldImages = post.getImageUrlList();
        List<String> newImages = requestDto.getImageUrls() != null ? requestDto.getImageUrls() : new ArrayList<>();

        for (String oldUrl : oldImages) {
            if (!newImages.contains(oldUrl)) {
                s3Service.deleteImage(oldUrl);
            }
        }

        // 시리즈 정보 업데이트
        String newSeries = post.getSeries();
        if (!post.getModel().equals(requestDto.getModel())) {
            Device device = deviceRepository.findByModel(requestDto.getModel())
                    .orElseThrow(() -> new CustomException(ErrorCode.DEVICE_NOT_FOUND));
            newSeries = device.getSeries();
        }

        post.updatePost(requestDto, newSeries);
        return SellPostResponse.from(post);
    }

    // 삭제
    @Transactional
    public void deletePost(Long postId, Long userId) {
        SellPost post = sellPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        validateWriter(post, userId);

        List<String> imageUrls = post.getImageUrlList();
        for (String url : imageUrls) {
            s3Service.deleteImage(url);
        }

        sellPostRepository.delete(post);
    }

    // 작성자 검증 로직
    private void validateWriter(SellPost post, Long userId) {
        if (!post.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    private List<SimilarPostResponse> getSimilarPosts(SellPost currentPost) {
        String currentSeries = currentPost.getSeries();

        List<String> sameSeriesModels = deviceRepository.findAllBySeries(currentSeries)
                .stream()
                .map(Device::getModel)
                .toList();

        // 유사 상품이 없을 경우 빈 리스트 반환
        if (sameSeriesModels.isEmpty()) {
            return List.of();
        }

        return sellPostRepository.findTop4ByManufacturerAndModelInAndIdNotOrderByCreatedAtDesc(
                        currentPost.getManufacturer(),
                        sameSeriesModels,
                        currentPost.getId()
                )
                .stream()
                .map(SimilarPostResponse::from)
                .collect(Collectors.toList());
    }
}