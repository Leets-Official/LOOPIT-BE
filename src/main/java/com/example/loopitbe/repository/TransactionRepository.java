package com.example.loopitbe.repository;

import com.example.loopitbe.entity.Transaction;
import com.example.loopitbe.enums.PostStatus; // 통합된 PostStatus 임포트
import com.example.loopitbe.enums.TransactionStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    long countByBuyer_UserId(Long userId);

    Optional<Transaction> findFirstBySellPost_IdOrderByCreatedAtDesc(Long postId);

    // 구매 내역 조회 (status가 null이면 전체 조회)
    @Query("SELECT t FROM Transaction t " +
            "JOIN FETCH t.sellPost " +
            "JOIN FETCH t.seller " +
            "WHERE t.buyer.userId = :buyerId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findAllBuyHistory(@Param("buyerId") Long buyerId, @Param("status") TransactionStatus status);

    // 판매 내역 조회 (status가 null이면 전체 조회)
    @Query("SELECT t FROM Transaction t " +
            "JOIN FETCH t.sellPost " +
            "JOIN FETCH t.buyer " +
            "WHERE t.seller.userId = :sellerId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findAllSellHistory(@Param("sellerId") Long sellerId, @Param("status") TransactionStatus status);
}