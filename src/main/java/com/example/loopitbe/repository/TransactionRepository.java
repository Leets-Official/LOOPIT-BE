package com.example.loopitbe.repository;

import com.example.loopitbe.entity.Transaction;
import com.example.loopitbe.enums.TransactionStatus;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findFirstBySellPost_IdOrderByCreatedAtDesc(Long postId);

    // 구매 내역 조회 (status가 null이면 전체 조회)
    @Query("SELECT t FROM Transaction t " +
            "JOIN FETCH t.sellPost " +
            "JOIN FETCH t.seller " +
            "WHERE t.buyer.userId = :buyerId " +
            "AND t.status != com.example.loopitbe.enums.TransactionStatus.CANCELED " + // 취소 제외
            "AND (:status IS NULL OR t.status = :status) " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findAllBuyHistory(@Param("buyerId") Long buyerId, @Param("status") TransactionStatus status);

    // 판매 내역 조회 (status가 null이면 전체 조회)
    @Query("SELECT t FROM Transaction t " +
            "JOIN FETCH t.sellPost " +
            "JOIN FETCH t.buyer " +
            "WHERE t.seller.userId = :sellerId " +
            "AND t.status != com.example.loopitbe.enums.TransactionStatus.CANCELED " + // 취소 제외
            "AND (:status IS NULL OR t.status = :status) " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findAllSellHistory(@Param("sellerId") Long sellerId, @Param("status") TransactionStatus status);
}