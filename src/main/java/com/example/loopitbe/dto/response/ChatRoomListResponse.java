package com.example.loopitbe.dto.response;

import com.example.loopitbe.entity.ChatRoom;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.User;

import java.time.LocalDateTime;

// 채팅방 목록 조회용
public class ChatRoomListResponse {
    private Long roomId;
    private Long postId;
    private Long partnerId;
    private String partnerNickname;
    private String partnerProfileImage;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private String thumbnail;

    // 안읽은 메시지 존재 여부
    private boolean hasUnreadMessages;

    public ChatRoomListResponse() {}

    public ChatRoomListResponse(
            Long roomId, Long postId, Long partnerId, String partnerNickname, String partnerProfileImage,
            String lastMessage, LocalDateTime lastMessageAt, String thumbnail,
            boolean hasUnreadMessages) {
        this.roomId = roomId;
        this.postId = postId;
        this.partnerId = partnerId;
        this.partnerNickname = partnerNickname;
        this.partnerProfileImage = partnerProfileImage;
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
        this.thumbnail = thumbnail;
        this.hasUnreadMessages = hasUnreadMessages;
    }

    public static ChatRoomListResponse from(ChatRoom room, Long myUserId, boolean hasUnreadMessages) {
        boolean isBuyer = room.getBuyer().getUserId().equals(myUserId);
        User partner = isBuyer ? room.getSeller() : room.getBuyer();

        SellPost post = room.getSellPost();
        String thumbnail = (post != null) ? post.getThumbnail() : null;

        return new ChatRoomListResponse(
                room.getId(),
                room.getSellPost().getId(),
                partner.getUserId(),
                partner.getNickname(),
                partner.getProfileImage(),
                room.getLastMessage(),
                room.getLastMessageAt(),
                thumbnail,
                hasUnreadMessages
        );
    }

    // Getters
    public Long getRoomId() { return roomId; }
    public Long getPostId() { return postId;}
    public Long getPartnerId() { return partnerId; }
    public String getPartnerNickname() { return partnerNickname; }
    public String getPartnerProfileImage() { return partnerProfileImage; }
    public String getLastMessage() { return lastMessage; }
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public String getThumbnail() { return thumbnail; }
    public boolean isHasUnreadMessages() { return hasUnreadMessages; }
}
