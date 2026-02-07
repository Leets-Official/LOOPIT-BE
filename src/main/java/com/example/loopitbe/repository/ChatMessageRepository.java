package com.example.loopitbe.repository;

import com.example.loopitbe.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 채팅방 ID로 모든 메시지 조회 (오래된 순서대로 조회하여 채팅창 위에서부터 채움)
    List<ChatMessage> findAllByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    // 특정 방에서 특정 유저가 보낸 메시지 외 안 읽은 메시지 조회
    List<ChatMessage> findAllByChatRoomIdAndSenderUserIdNotAndIsReadFalse(Long chatRoomId, Long userId);

    // 해당 방에 내가 안 읽은 메시지가 존재하는지 확인
    boolean existsByChatRoomIdAndSenderUserIdNotAndIsReadFalse(Long chatRoomId, Long userId);

    boolean existsByReceiverUserIdAndIsReadFalse(Long receiverId);
}
