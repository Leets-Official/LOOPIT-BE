package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.SellPostSearchCondition;
import com.example.loopitbe.dto.response.*;
import com.example.loopitbe.dto.request.SellPostRequest;
import com.example.loopitbe.entity.Device;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.repository.DeviceRepository;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.SellPostSpecification;
import com.example.loopitbe.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellPostService {

    private final SellPostRepository sellPostRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    public SellPostService(SellPostRepository sellPostRepository, UserRepository userRepository, DeviceRepository deviceRepository) {
        this.sellPostRepository = sellPostRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public SellPostResponse createPost(String kakaoId, SellPostRequest requestDto) {
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 시리즈 정보 저장
        Device device = deviceRepository.findByModel(requestDto.getModelName())
                .orElseThrow(() -> new CustomException(ErrorCode.DEVICE_NOT_FOUND));

        String series = device.getSeries();

        SellPost sellPost = SellPost.createPost(user, requestDto, series);
        SellPost savedPost = sellPostRepository.save(sellPost);

        return SellPostResponse.from(savedPost);
    }

    // 목록 조회
    @Transactional(readOnly = true)
    public Page<SellPostListResponse> getSellPosts(int page, SellPostSearchCondition condition) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
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