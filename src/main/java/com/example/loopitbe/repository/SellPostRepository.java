package com.example.loopitbe.repository;

import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SellPostRepository extends JpaRepository<SellPost, Long>, JpaSpecificationExecutor<SellPost> {
    Page<SellPost> findAllByUser_UserIdAndIsDeletedFalse(Long userId, Pageable pageable);
    // 동일 제조사, 모델명이 리스트에 포함 / 현재 게시글 제외
    List<SellPost> findTop4ByManufacturerAndModelInAndIdNotAndIsDeletedFalseOrderByCreatedAtDesc(
            String manufacturer,
            List<String> models,
            Long currentPostId
    );
    Optional<SellPost> findByIdAndIsDeletedFalse(Long id);
    List<SellPost> findAllByUser_UserIdAndStatusAndIsDeletedFalse(Long userId, PostStatus status);
}