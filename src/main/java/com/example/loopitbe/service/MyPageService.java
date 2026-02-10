package com.example.loopitbe.service;

import com.example.loopitbe.dto.response.MyPageResponse;
import com.example.loopitbe.entity.Transaction;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.UserRepository;
import com.example.loopitbe.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public MyPageService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // 마이페이지 초기 정보 로드 (사용자 정보 + 전체 구매 내역)
    @Transactional(readOnly = true)
    public MyPageResponse getMyPageInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Transaction> buyList = transactionRepository.findAllBuyHistory(userId, null);

        return MyPageResponse.from(user, buyList);
    }
}