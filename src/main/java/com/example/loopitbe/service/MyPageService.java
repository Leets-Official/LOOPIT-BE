package com.example.loopitbe.service;

import com.example.loopitbe.dto.response.MyPageResponse;
import com.example.loopitbe.dto.request.ProfileUpdateRequest;
import com.example.loopitbe.dto.response.TradeHistoryResponse;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.repository.UserRepository;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.TransactionRepository;
import com.example.loopitbe.repository.WishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final SellPostRepository sellPostRepository;
    private final TransactionRepository transactionRepository;
    private final WishListRepository wishListRepository;
    private final S3Service s3Service;

    // 명시적 생성자 주입
    public MyPageService(UserRepository userRepository,
                         SellPostRepository sellPostRepository,
                         TransactionRepository transactionRepository,
                         WishListRepository wishListRepository, // 파라미터명 오타 수정
                         S3Service s3Service) {
        this.userRepository = userRepository;
        this.sellPostRepository = sellPostRepository;
        this.transactionRepository = transactionRepository;
        this.wishListRepository = wishListRepository;
        this.s3Service = s3Service;
    }

    /**
     * 마이페이지 메인 정보 조회 (kakaoId 기준)
     */
    @Transactional(readOnly = true)
    public MyPageResponse getMyPageInfo(String kakaoId) {
        // 1. findByKakaoId 사용 (UserRepository에 이미 존재)
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 활동 수치 조회 (kakaoId 기준 메서드로 호출)
        long sellCount = sellPostRepository.countByUser_KakaoId(kakaoId);
        long buyCount = transactionRepository.countByBuyerKakaoId(kakaoId);
        long wishCount = wishListRepository.countByUser_KakaoId(kakaoId);

        // 프로필 이미지 S3 URL 생성
        String profileImageUrl = s3Service.getPresignedUrl("profiles", user.getProfileImage());

        return MyPageResponse.of(user, profileImageUrl, sellCount, buyCount, wishCount);
    }

    /**
     * 거래 내역 조회 (kakaoId 기준)
     */
    @Transactional(readOnly = true)
    public List<TradeHistoryResponse> getTradeHistory(String kakaoId, String type, String status) {
        if ("BUY".equalsIgnoreCase(type)) {
            return transactionRepository.findTransactionsByKakaoId(kakaoId, status).stream()
                    .map(tx -> {
                        // S3Service의 메서드 정의에 따라 인자 개수를 맞춰야 합니다.
                        String url = s3Service.getPresignedUrl("posts", tx.getPost().getThumbnail());
                        return TradeHistoryResponse.from(tx, url); // 이제 정확히 (Transaction, String) 호출
                    })
                    .collect(Collectors.toList());
        } else {
            // 판매 내역 조회 로직
            return sellPostRepository.findPostsByUserKakaoIdAndStatus(kakaoId, status).stream()
                    .map(post -> TradeHistoryResponse.from(post,
                            s3Service.getPresignedUrl("posts", post.getImageUrls().isEmpty() ? null : post.getImageUrls().get(0))))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 개인정보 수정
     */
    @Transactional
    public void updateProfile(String kakaoId, ProfileUpdateRequest request) {
        // 1. 유저 정보 조회
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 닉네임 중복 검사 (닉네임이 변경된 경우에만)
        if (!user.getNickname().equals(request.nickname())) {
            if (userRepository.existsUserByNickname(request.nickname())) {
                throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
            }
        }

        user.updateUser(
                request.nickname(),
                request.name(),
                request.email(),
                request.birthdate()
        );

    }

    /**
     * S3 업로드용 Presigned URL 발급
     */
    public String getUploadUrl(String kakaoId, String fileName) {
        // S3Service의 getPresignedUrl(String prefix, String fileName) 형식을 따름
        return s3Service.getPresignedUrl("profiles/" + kakaoId, fileName);
    }
}