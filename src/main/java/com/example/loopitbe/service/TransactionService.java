package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.CancelTransactionRequest;
import com.example.loopitbe.dto.request.CompleteTransactionRequest;
import com.example.loopitbe.dto.request.CreateTransactionRequest;
import com.example.loopitbe.dto.response.MySellListResponse;
import com.example.loopitbe.dto.response.TransactionHistoryResponse;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.Transaction;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.enums.PostStatus;
import com.example.loopitbe.enums.TransactionStatus;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.TransactionRepository;
import com.example.loopitbe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellPostRepository sellPostRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, SellPostRepository sellPostRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.sellPostRepository = sellPostRepository;
        this.userRepository = userRepository;
    }

    // 판매자가 예약중으로 변경 시 Transaction 시작.
    @Transactional
    public TransactionHistoryResponse createTransaction(CreateTransactionRequest request, Long userId) {
        // 판매글에 대해 진행 중 또는 완료된 거래 존재하는 지 확인
        // 기존 예약중, 거래완료 거래는 취소 처리
        transactionRepository.findFirstBySellPost_IdOrderByCreatedAtDesc(request.getPostId())
                .ifPresent(tr -> {
                    tr.updateStatus(TransactionStatus.CANCELED);
                });

        SellPost post = sellPostRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        // 판매자 본인이 아닌 경우 예외 처리
        if (!post.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 판매 게시글 상태도 같이 업데이트 시킴
        post.updateStatus(PostStatus.RESERVED);

        Transaction newTr = Transaction.createTransaction(post, buyer, post.getUser());

        return TransactionHistoryResponse.from(transactionRepository.save(newTr));
    }

    @Transactional
    public TransactionHistoryResponse completeTransaction(CompleteTransactionRequest request, Long userId){
        SellPost post = sellPostRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 판매자 본인이 아닌 경우 예외 처리
        if (!post.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Transaction transaction = transactionRepository.findFirstBySellPost_IdOrderByCreatedAtDesc(request.getPostId())
                .orElse(null);

        // 예약 중, 거래완료가 아닌 경우 -> 거래완료로 거래 새로 만들어서 저장
        if (transaction == null) {
            transaction = transactionRepository.save(Transaction.createTransaction(post, buyer, post.getUser()));
            post.updateStatus(PostStatus.COMPLETED);
        }
        else{
            // 거래에 해당하는 구매자와 일치하지 않는 경우
            if (!transaction.getBuyer().getUserId().equals(request.getBuyerId())){
                throw new CustomException(ErrorCode.NOT_THE_BUYER);
            }
            // 이미 거래 완료된 거래인 경우(에러 처리)
            if (transaction.getStatus().equals(TransactionStatus.COMPLETED)){
                throw new CustomException(ErrorCode.TRANSACTION_ALREADY_COMPLETED);
            }
            // 예약 중인 경우
            else{
                transaction.updateStatus(TransactionStatus.COMPLETED);
                post.updateStatus(PostStatus.COMPLETED);
            }
        }

        return TransactionHistoryResponse.from(transaction);
    }

    @Transactional
    public TransactionHistoryResponse cancelTransaction(CancelTransactionRequest request, Long userId){
        SellPost post = sellPostRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 판매자 본인이 아닌 경우 예외 처리
        if (!post.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 판매글에 대해 진행 중인 거래(예약 중) 혹은 거래완료된 거래 존재하는지 확인
        Transaction transaction = transactionRepository.findFirstBySellPost_IdOrderByCreatedAtDesc(request.getPostId())
                .filter(tr -> tr.getStatus() != TransactionStatus.CANCELED)
                .orElseThrow(() -> new CustomException(ErrorCode.ONGOING_OR_COMPLETED_TRANSACTION_NOT_FOUND));

        transaction.updateStatus(TransactionStatus.CANCELED);
        post.updateStatus(PostStatus.SALE);

        return  TransactionHistoryResponse.from(transaction);
    }

    // 구매 내역 조회
    @Transactional(readOnly = true)
    public List<TransactionHistoryResponse> getBuyHistory(Long userId, String statusStr) {
        TransactionStatus status;
        // statusStr에 ALL(전체), RESERVED(예약중), COMPLETED(판매완료) 이외의 값은 예외처리
        try {
            // ALL인 경우 status에 null값
            status = "ALL".equals(statusStr) ? null : TransactionStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_STATUS_VALUE);
        }

        List<Transaction> buyHistory = transactionRepository.findAllBuyHistory(userId, status);

        return buyHistory.stream().map(TransactionHistoryResponse::from).toList();
    }

    // 판매 내역 조회
    @Transactional(readOnly = true)
    public List<MySellListResponse> getSellHistory(Long userId, String statusStr) {
        PostStatus status;
        // statusStr에 ALL(전체), SALE(판매중), RESERVED(예약중), COMPLETED(판매완료) 이외의 값은 예외처리
        try {
            // ALL인 경우 status에 null값
            status = "ALL".equals(statusStr) ? null : PostStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_STATUS_VALUE);
        }

        List<SellPost> sellHistory = sellPostRepository.findAllByUser_UserIdAndStatusAndIsDeletedFalse(userId, status);

        return sellHistory.stream().map(MySellListResponse::from).toList();
    }
}