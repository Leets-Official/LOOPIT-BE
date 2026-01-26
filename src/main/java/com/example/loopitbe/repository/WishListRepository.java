package com.example.loopitbe.repository;

import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    Optional<WishList> findByUser_UserIdAndSellPost_Id(Long userId, Long postId);
    @Query("SELECT w FROM WishList w JOIN FETCH w.sellPost WHERE w.user.userId = :userId")
    List<WishList> findAllByUserId(Long userId);

    long countByUser_KakaoId(String kakaoId); //찜 카운팅 추가
}
