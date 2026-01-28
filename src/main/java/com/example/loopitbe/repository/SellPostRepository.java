package com.example.loopitbe.repository;

import com.example.loopitbe.entity.SellPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellPostRepository extends JpaRepository<SellPost, Long> {
    Page<SellPost> findAllByUser_UserId(Long userId, Pageable pageable);
}