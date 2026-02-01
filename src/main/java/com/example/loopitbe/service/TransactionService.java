package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.CompleteTransactionRequest;
import com.example.loopitbe.dto.request.CreateTransactionRequest;
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
    public TransactionHistoryResponse createTransaction(CreateTransactionRequest request) {
        // 판매글에 대해 진행 중 또는 완료된 거래 존재하는 지 확인
        transactionRepository.findFirstBySellPost_IdOrderByCreatedAtDesc(request.getPostId())
                .ifPresent(tr -> {
                    if (tr.getStatus() != TransactionStatus.CANCELED) {
                        throw new CustomException(ErrorCode.TRANSACTION_ALREADY_EXISTS);
                    }
                });

        SellPost post = sellPostRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        // 판매자 본인이 아닌 경우 예외 처리
        if (!post.getUser().getUserId().equals(request.getUserId())) {
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
    public TransactionHistoryResponse completeTransaction(CompleteTransactionRequest request){
        SellPost post = sellPostRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 판매자 본인이 아닌 경우 예외 처리
        if (!post.getUser().getUserId().equals(request.getUserId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 판매글에 대해 진행 중인 거래(예약 중) 존재하는지 확인
        Transaction transaction = transactionRepository.findFirstBySellPost_IdOrderByCreatedAtDesc(request.getPostId())
                .filter(tr -> tr.getStatus() == TransactionStatus.RESERVED)
                .orElseThrow(() -> new CustomException(ErrorCode.ONGOING_TRANSACTION_NOT_FOUND));

        transaction.updateStatus(TransactionStatus.COMPLETED);
        post.updateStatus(PostStatus.COMPLETED);

        return  TransactionHistoryResponse.from(transactionRepository.save(transaction));
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
    public List<TransactionHistoryResponse> getSellHistory(Long userId, String statusStr) {
        TransactionStatus status;
        // statusStr에 ALL(전체), RESERVED(예약중), COMPLETED(판매완료) 이외의 값은 예외처리
        try {
            // ALL인 경우 status에 null값
            status = "ALL".equals(statusStr) ? null : TransactionStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_STATUS_VALUE);
        }

        List<Transaction> sellHistory = transactionRepository.findAllSellHistory(userId, status);

        return sellHistory.stream().map(TransactionHistoryResponse::from).toList();
    }
}