package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.ChatMessageRequest;
import com.example.loopitbe.dto.request.ChatRoomCreateRequest;
import com.example.loopitbe.dto.response.ChatMessageResponse;
import com.example.loopitbe.dto.response.ChatRoomDetailResponse;
import com.example.loopitbe.dto.response.ChatRoomListResponse;
import com.example.loopitbe.entity.ChatMessage;
import com.example.loopitbe.entity.ChatRoom;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.ChatMessageRepository;
import com.example.loopitbe.repository.ChatRoomRepository;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SellPostRepository sellPostRepository;

    public ChatService(ChatRoomRepository chatRoomRepository,
                       ChatMessageRepository chatMessageRepository,
                       UserRepository userRepository,
                       SellPostRepository sellPostRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.sellPostRepository = sellPostRepository;
    }

    // 메시지 저장
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

    // 채팅방 생성 및 조회 메서드
    @Transactional
    public ChatRoomDetailResponse createOrGetChatRoom(ChatRoomCreateRequest request) {
        // 1. 판매글 존재 여부 확인
        SellPost sellPost = sellPostRepository.findById(request.getSellPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 2. 이미 존재하는 채팅방인지 확인
        return chatRoomRepository.findByBuyerUserIdAndSellerUserIdAndSellPostId(
                request.getBuyerId(), request.getSellerId(), request.getSellPostId())
                .map(ChatRoomDetailResponse::from)
                .orElseGet(() -> {
                    // 3. 존재하지 않으면 새로 생성
                    User buyer = userRepository.findById(request.getBuyerId())
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                    User seller = userRepository.findById(request.getSellerId())
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                    ChatRoom newRoom = new ChatRoom(buyer, seller, sellPost);
                    ChatRoom savedRoom = chatRoomRepository.save(newRoom);
                    return ChatRoomDetailResponse.from(savedRoom);
                });
    }

    // 모든 채팅방 조회
    @Transactional(readOnly = true)
    public List<ChatRoomListResponse> getMyChatRooms(Long userId) {
        // 사용자가 구매자이거나 판매자인 모든 방 조회
        List<ChatRoom> rooms = chatRoomRepository.findAllByBuyerUserIdOrSellerUserIdOrderByLastMessageAtDesc(userId, userId);

        List<ChatRoomListResponse> responseList = new ArrayList<>();
        for (ChatRoom room : rooms) {
            responseList.add(ChatRoomListResponse.from(room, userId));
        }
        return responseList;
    }

    // 이전 채팅 내역 확인
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatMessages(Long roomId) {
        // 1. 채팅방 존재 여부 확인
        if (!chatRoomRepository.existsById(roomId)) {
            throw new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND);
        }

        // 2. 메시지 내역 조회
        List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(roomId);

        // 3. DTO 변환
        List<ChatMessageResponse> responseList = new ArrayList<>();
        for (ChatMessage message : messages) {
            responseList.add(ChatMessageResponse.from(message));
        }

        return responseList;
    }

    // 읽음 표시
    @Transactional
    public void markMessagesAsRead(Long roomId, Long userId) {
        // 해당 방에서 상대방이 보낸 안 읽은 메시지들을 모두 가져옴
        List<ChatMessage> unreadMessages = chatMessageRepository
                .findAllByChatRoomIdAndSenderUserIdNotAndIsReadFalse(roomId, userId);

        // 읽음 처리
        if (!unreadMessages.isEmpty()) {
            for (ChatMessage message : unreadMessages) {
                message.markAsRead();
            }
        }
    }
}
