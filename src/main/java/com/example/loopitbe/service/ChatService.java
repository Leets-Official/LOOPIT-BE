package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.ChatMessageRequest;
import com.example.loopitbe.dto.response.ChatMessageResponse;
import com.example.loopitbe.entity.ChatMessage;
import com.example.loopitbe.entity.ChatRoom;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.ChatMessageRepository;
import com.example.loopitbe.repository.ChatRoomRepository;
import com.example.loopitbe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRoomRepository chatRoomRepository,
                       ChatMessageRepository chatMessageRepository,
                       UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    public ChatMessageResponse saveMessage(ChatMessageRequest request) {
        // 1. 채팅방 및 유저 조회
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 2. 메시지 엔티티 생성 및 저장
        ChatMessage message = new ChatMessage(
                room,
                sender,
                request.getType(),
                request.getContent()
        );
        chatMessageRepository.save(message);

        // 3. 채팅방 최신 메시지 정보 업데이트
        room.updateLastMessage(request.getContent(), LocalDateTime.now());

        // 4. 응답 DTO로 변환하여 반환
        return ChatMessageResponse.from(message);
    }
}
