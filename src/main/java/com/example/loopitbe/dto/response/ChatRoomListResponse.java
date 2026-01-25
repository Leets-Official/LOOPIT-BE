package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.ChatRoom;
import com.example.loopitbe.entity.User;

import java.time.LocalDateTime;

// 채팅방 목록 조회용
public class ChatRoomListResponse {
    private Long roomId;
    private Long partnerId;
    private String partnerNickname;
    private String partnerProfileImage;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    private String postTitle; // 제목 말고 기종으로 변경?
    private String postImage;

    // 안읽은 메시지 존재 여부
    private boolean hasUnreadMessages;

    public ChatRoomListResponse() {}

    public ChatRoomListResponse(
            Long roomId, Long partnerId, String partnerNickname, String partnerProfileImage,
            String lastMessage, LocalDateTime lastMessageAt, String postTitle, String postImage,
            boolean hasUnreadMessages) {
        this.roomId = roomId;
        this.partnerId = partnerId;
        this.partnerNickname = partnerNickname;
        this.partnerProfileImage = partnerProfileImage;
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
        this.postTitle = postTitle;
        this.postImage = postImage;
        this.hasUnreadMessages = hasUnreadMessages;
    }

    public static ChatRoomListResponse from(ChatRoom room, Long myUserId, boolean hasUnreadMessages) {
        boolean isBuyer = room.getBuyer().getUserId().equals(myUserId);
        User partner = isBuyer ? room.getSeller() : room.getBuyer();

        String thumbnail = (room.getSellPost().getImageUrls() != null && !room.getSellPost().getImageUrls().isEmpty())
                ? room.getSellPost().getImageUrls().get(0) : null;

        return new ChatRoomListResponse(
                room.getId(),
                partner.getUserId(),
                partner.getNickname(),
                partner.getProfileImage(),
                room.getLastMessage(),
                room.getLastMessageAt(),
                room.getSellPost().getTitle(),
                thumbnail,
                hasUnreadMessages
        );
    }

    // Getters
    public Long getRoomId() { return roomId; }
    public Long getPartnerId() { return partnerId; }
    public String getPartnerNickname() { return partnerNickname; }
    public String getPartnerProfileImage() { return partnerProfileImage; }
    public String getLastMessage() { return lastMessage; }
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public String getPostTitle() { return postTitle; }
    public String getPostImage() { return postImage; }
    public boolean isHasUnreadMessages() { return hasUnreadMessages; }
}
