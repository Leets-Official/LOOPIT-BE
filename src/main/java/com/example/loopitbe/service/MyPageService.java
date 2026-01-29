package com.example.loopitbe.service;

import com.example.loopitbe.dto.response.MyPageResponse;
import com.example.loopitbe.dto.request.ProfileUpdateRequest;
import com.example.loopitbe.dto.response.TradeHistoryResponse;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.Transaction;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.enums.PostStatus;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.UserRepository;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.TransactionRepository;
import com.example.loopitbe.repository.WishListRepository;
import org.springframework.data.domain.Sort;
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

    public MyPageService(UserRepository userRepository,
                         SellPostRepository sellPostRepository,
                         TransactionRepository transactionRepository,
                         WishListRepository wishListRepository,
                         S3Service s3Service) {
        this.userRepository = userRepository;
        this.sellPostRepository = sellPostRepository;
        this.transactionRepository = transactionRepository;
        this.wishListRepository = wishListRepository;
        this.s3Service = s3Service;
    }

    /**
     * 마이페이지 메인 정보 조회
     */
    @Transactional(readOnly = true)
    public MyPageResponse getMyPageInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        long sellCount = sellPostRepository.countByUser_UserId(userId);
        long buyCount = transactionRepository.countByBuyer_UserId(userId);
        long wishCount = wishListRepository.countByUser_UserId(userId);

        String profileImageUrl = s3Service.getPresignedUrl("profiles", user.getProfileImage());

        return MyPageResponse.of(user, profileImageUrl, sellCount, buyCount, wishCount);
    }

    /**
     * 거래 내역 조회
     */
    @Transactional(readOnly = true)
    public List<TradeHistoryResponse> getTradeHistory(Long userId, String type, String status) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        if ("BUY".equalsIgnoreCase(type)) {
            List<Transaction> transactions;
            if ("ALL".equalsIgnoreCase(status)) {
                transactions = transactionRepository.findAllByBuyer_UserId(userId, sort);
            } else {
                // String status를 PostStatus Enum으로 변환
                PostStatus postStatus = PostStatus.valueOf(status.toUpperCase());
                transactions = transactionRepository.findAllByBuyer_UserIdAndStatus(userId, postStatus, sort);
            }

            return transactions.stream()
                    .map(tx -> {
                        String url = s3Service.getPresignedUrl("posts", tx.getPost().getThumbnail());
                        return TradeHistoryResponse.from(tx, url);
                    })
                    .collect(Collectors.toList());
        } else {
            List<SellPost> posts;
            if ("ALL".equalsIgnoreCase(status)) {
                posts = sellPostRepository.findAllByUser_UserId(userId, sort);
            } else {
                // String status를 PostStatus Enum으로 변환
                PostStatus postStatus = PostStatus.valueOf(status.toUpperCase());
                posts = sellPostRepository.findAllByUser_UserIdAndStatus(userId, postStatus, sort);
            }

            return posts.stream()
                    .map(post -> TradeHistoryResponse.from(post,
                            s3Service.getPresignedUrl("posts", post.getImageUrls().isEmpty() ? null : post.getImageUrls().get(0))))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 개인정보 수정
     */
    @Transactional
    public void updateProfile(Long userId, ProfileUpdateRequest request, String newProfileImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 닉네임 중복 체크
        if (!user.getNickname().equals(request.getNickname())) {
            if (userRepository.existsUserByNickname(request.getNickname())) {
                throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
            }
        }

        // 이미지 삭제 및 업데이트
        if (newProfileImage != null && !newProfileImage.equals(user.getProfileImage())) {
            if (user.getProfileImage() != null) {
                s3Service.deleteFile(user.getProfileImage());
            }
            user.updateProfileImage(newProfileImage);
        }

        user.updateUser(request);
    }
}