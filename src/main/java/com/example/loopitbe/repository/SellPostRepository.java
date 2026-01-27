package com.example.loopitbe.repository;

import com.example.loopitbe.entity.SellPost;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellPostRepository extends JpaRepository<SellPost, Long> {

    // 1. 유저 ID(PK)로 총 판매글 개수 조회 (countBy + 연관필드_PK필드)
    long countByUser_UserId(Long userId);

    // 2. 특정 상태의 판매 내역 조회 (ALL이 아닐 때 사용)
    // find + All + By + User_UserId + And + Status
    List<SellPost> findAllByUser_UserIdAndStatus(Long userId, String status, Sort sort);

    // 3. 전체 판매 내역 조회 (ALL일 때 사용)
    List<SellPost> findAllByUser_UserId(Long userId, Sort sort);
}