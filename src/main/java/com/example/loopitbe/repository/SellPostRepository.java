package com.example.loopitbe.repository;

import com.example.loopitbe.entity.SellPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellPostRepository extends JpaRepository<SellPost, Long>, JpaSpecificationExecutor<SellPost> {

    // 동일 제조사, 모델명이 리스트에 포함 / 현재 게시글 제외
    List<SellPost> findTop4ByManufacturerAndModelInAndIdNotOrderByCreatedAtDesc(
            String manufacturer,
            List<String> models,
            Long currentPostId
    );
}