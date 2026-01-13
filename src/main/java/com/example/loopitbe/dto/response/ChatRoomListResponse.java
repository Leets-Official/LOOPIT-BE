package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.ChatRoom;
import com.example.loopitbe.entity.User;

import java.time.LocalDateTime;

// 채팅방 목록 조회용
public class ChatRoomListResponse {
    private Long roomId;
    private Long partnerId;
    private String partnerNickname;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    // 추후 SellPost 관련 필드 추가

    public ChatRoomListResponse() {}

    public ChatRoomListResponse(Long roomId, Long partnerId, String partnerNickname, String lastMessage, LocalDateTime lastMessageAt) {
        this.roomId = roomId;
        this.partnerId = partnerId;
        this.partnerNickname = partnerNickname;
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
    }

    public static ChatRoomListResponse from(ChatRoom room, Long myUserId) {
        boolean isBuyer = room.getBuyer().getUserId().equals(myUserId);
        User partner = isBuyer ? room.getSeller() : room.getBuyer();

        return new ChatRoomListResponse(
                room.getId(),
                partner.getUserId(),
                partner.getNickname(),
                room.getLastMessage(),
                room.getLastMessageAt()
        );
    }

    // Getters
    public Long getRoomId() { return roomId; }
    public Long getPartnerId() { return partnerId; }
    public String getPartnerNickname() { return partnerNickname; }
    public String getLastMessage() { return lastMessage; }
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
}
