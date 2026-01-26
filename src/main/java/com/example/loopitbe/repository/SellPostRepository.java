package com.example.loopitbe.repository;

import com.example.loopitbe.entity.SellPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // 두 기능을 모두 쓰기 위해 둘 다 유지

@Repository
public interface SellPostRepository extends JpaRepository<SellPost, Long> {

    // 1. 마이페이지 메인용: 유저가 올린 총 판매글 개수 조회
    long countByUser_KakaoId(String kakaoId);
    /**
     status가 'ALL'이면 전체 조회, 아니면 해당 상태(판매중, 예약중 등)만 조회
     **/
    @Query("SELECT s FROM SellPost s WHERE s.user.kakaoId = :kakaoId " +
            "AND (:status = 'ALL' OR s.status = :status) " +
            "ORDER BY s.createdAt DESC")
    List<SellPost> findPostsByUserKakaoIdAndStatus(@Param("kakaoId") String kakaoId, @Param("status") String status);
}