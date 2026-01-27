package com.example.loopitbe.repository;

import com.example.loopitbe.entity.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 1. 내가 구매한 총 횟수 (userId 기준)
    // 연관 객체 buyer의 userId 필드를 참조 (언더바(_)로 경로 구분)
    long countByBuyer_UserId(Long userId);

    // 2. 구매 내역 리스트 조회 (단순 상태별 조회)
    // 이름만으로 쿼리 생성: find + All + By + Buyer_UserId + And + Status
    List<Transaction> findAllByBuyer_UserIdAndStatus(Long userId, String status, Sort sort);

    // 3. 전체 조회가 필요할 때를 위한 메서드
    List<Transaction> findAllByBuyer_UserId(Long userId, Sort sort);
}