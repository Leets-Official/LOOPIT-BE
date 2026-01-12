package com.example.loopitbe.repository;

import com.example.loopitbe.entity.SellPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellPostRepository extends JpaRepository<SellPost, Long> { }