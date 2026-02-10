package com.example.loopitbe.repository;

import com.example.loopitbe.entity.PostWishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostWishListRepository extends JpaRepository<PostWishList, Long> {
    Optional<PostWishList> findByUser_UserIdAndSellPost_Id(Long userId, Long postId);
    @Query("SELECT w FROM PostWishList w JOIN FETCH w.sellPost WHERE w.user.userId = :userId")
    List<PostWishList> findAllByUserId(Long userId);

    boolean existsByUser_UserIdAndSellPost_Id(Long userId, Long postId);
}
