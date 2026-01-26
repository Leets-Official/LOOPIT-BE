package com.example.loopitbe.repository;

import com.example.loopitbe.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 구매자, 판매자, 판매글 ID로 기존 방이 있는지 조회
    Optional<ChatRoom> findByBuyerUserIdAndSellerUserIdAndSellPostId(Long buyerId, Long sellerId, Long sellPostId);

    // 사용자 본인이 포함된 모든 채팅방을 최신 메시지 순으로 조회
    List<ChatRoom> findAllByBuyerUserIdOrSellerUserIdOrderByLastMessageAtDesc(Long buyerId, Long sellerId);
}
