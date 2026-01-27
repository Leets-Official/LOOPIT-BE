package com.example.loopitbe.repository;

import com.example.loopitbe.entity.Transaction;
import com.example.loopitbe.enums.PostStatus; // 통합된 PostStatus 임포트
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    long countByBuyer_UserId(Long userId);

    List<Transaction> findAllByBuyer_UserIdAndStatus(Long userId, PostStatus status, Sort sort);

    List<Transaction> findAllByBuyer_UserId(Long userId, Sort sort);
}