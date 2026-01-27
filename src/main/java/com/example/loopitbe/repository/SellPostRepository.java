package com.example.loopitbe.repository;

import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.enums.PostStatus; // Enum 임포트
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellPostRepository extends JpaRepository<SellPost, Long> {

    long countByUser_UserId(Long userId);

    List<SellPost> findAllByUser_UserIdAndStatus(Long userId, PostStatus status, Sort sort);

    List<SellPost> findAllByUser_UserId(Long userId, Sort sort);
}