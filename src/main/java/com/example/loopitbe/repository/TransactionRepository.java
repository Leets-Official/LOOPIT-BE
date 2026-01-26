package com.example.loopitbe.repository;

import com.example.loopitbe.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 1. 내가 구매한 총 횟수 (kakaoId 기준)
    long countByBuyerKakaoId(String kakaoId);

    // 2. 구매 내역 리스트 조회 (kakaoId 기준)
    @Query("SELECT t FROM Transaction t WHERE t.buyerKakaoId = :kakaoId " +
            "AND (:status = 'ALL' OR t.status = :status) " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findTransactionsByKakaoId(@Param("kakaoId") String kakaoId, @Param("status") String status);
}