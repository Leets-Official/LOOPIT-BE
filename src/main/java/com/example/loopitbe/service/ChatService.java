package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.ChatMessageRequest;
import com.example.loopitbe.dto.request.ChatRoomCreateRequest;
import com.example.loopitbe.dto.response.ChatMessageResponse;
import com.example.loopitbe.dto.response.ChatRoomDetailResponse;
import com.example.loopitbe.dto.response.ChatRoomListResponse;
import com.example.loopitbe.entity.*;
import com.example.loopitbe.enums.MessageType;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import com.example.loopitbe.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SellPostRepository sellPostRepository;
    private final ChatImageRepository chatImageRepository;

    public ChatService(ChatRoomRepository chatRoomRepository,
                       ChatMessageRepository chatMessageRepository,
                       UserRepository userRepository,
                       SellPostRepository sellPostRepository,
                       ChatImageRepository chatImageRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.sellPostRepository = sellPostRepository;
        this.chatImageRepository = chatImageRepository;
    }

    // 메시지 저장
    public ChatMessageResponse saveMessage(ChatMessageRequest request, Long userId) {
        // 1. 채팅방 및 유저 조회
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        // 2. 메시지 내용 처리 (이미지일 경우, 실제 URL은 ChatImage에 저장)
        String msgContent = request.getContent();
        if (request.getType() == MessageType.IMAGE) {
            msgContent = "이미지를 보냈습니다."; // 채팅방 목록에서 보여줄 대체 텍스트
        }

        // 3. 메시지 엔티티 생성 및 저장
        ChatMessage message = new ChatMessage(
                room,
                sender,
                receiver,
                request.getType(),
                msgContent
        );
        chatMessageRepository.save(message);

        // 4. 이미지 타입일 경우 ChatImage 테이블에 별도 저장
        if (request.getType() == MessageType.IMAGE) {
            ChatImage chatImage = new ChatImage(message, request.getContent());
            chatImageRepository.save(chatImage);
        }

        // 5. 채팅방 최신 메시지 정보 업데이트
        room.updateLastMessage(msgContent, LocalDateTime.now());

        // 6. 응답 DTO로 변환하여 반환
        return ChatMessageResponse.from(message);
    }

    // 채팅방 생성 및 조회 메서드
    @Transactional
    public ChatRoomDetailResponse createChatRoom(Long buyerId, Long postId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        SellPost sellPost = sellPostRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User seller = userRepository.findById(sellPost.getUser().getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 1. 이미 존재하는 채팅방인지 먼저 확인 (기존 참여자라면 게시글 삭제 여부와 상관없이 입장)
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByBuyerUserIdAndSellerUserIdAndSellPostId(
                buyerId, seller.getUserId(), postId);

        if (existingRoom.isPresent()) {
            return ChatRoomDetailResponse.from(existingRoom.get());
        }

        // 3. 신규 채팅방 생성
        ChatRoom newRoom = new ChatRoom(buyer, seller, sellPost);
        ChatRoom savedRoom = chatRoomRepository.save(newRoom);

        return ChatRoomDetailResponse.from(savedRoom);
    }

    public ChatRoomDetailResponse getChatRoom(Long userId, Long partnerId, Long postId){
        SellPost sellPost = sellPostRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User seller = userRepository.findById(sellPost.getUser().getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean isSeller = seller.getUserId().equals(userId);

        Optional<ChatRoom> existingRoom = chatRoomRepository.findByBuyerUserIdAndSellerUserIdAndSellPostId(
                isSeller ? partnerId : userId, seller.getUserId(), postId);

        if (existingRoom.isPresent())
            return ChatRoomDetailResponse.from(existingRoom.get());
        else
            throw new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND);
    }

    // 모든 채팅방 조회
    @Transactional(readOnly = true)
    public List<ChatRoomListResponse> getMyChatRooms(Long userId) {
        // 사용자가 구매자이거나 판매자인 모든 방 조회
        List<ChatRoom> rooms = chatRoomRepository.findAllByBuyerUserIdOrSellerUserIdOrderByLastMessageAtDesc(userId, userId);

        List<ChatRoomListResponse> responseList = new ArrayList<>();

        for (ChatRoom room : rooms) {
            boolean hasUnread = chatMessageRepository.existsByChatRoomIdAndSenderUserIdNotAndIsReadFalse(room.getId(), userId);
            responseList.add(ChatRoomListResponse.from(room, userId, hasUnread));
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

    public boolean existsUnreadMessages(Long userId){
        return chatMessageRepository
                .existsByReceiverUserIdAndIsReadFalse(userId);
    }
}
