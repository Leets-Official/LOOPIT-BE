package com.example.loopitbe.entity;

import com.example.loopitbe.enums.MessageType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "message_type")
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(columnDefinition = "TEXT")
    private String content; // TEXT 타입일 때만 내용 존재

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false; // 읽음 여부 (default: false)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 기본 생성자
    protected ChatMessage() {}

    public ChatMessage(ChatRoom chatRoom, User sender, MessageType messageType, String content) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.messageType = messageType;
        this.content = content;
        this.isRead = false;
    }

    // 메시지 읽음 처리 메서드
    public void markAsRead() {
        this.isRead = true;
    }

    // Getters
    public Long getId() { return id; }
    public ChatRoom getChatRoom() { return chatRoom; }
    public User getSender() { return sender; }
    public MessageType getMessageType() { return messageType; }
    public String getContent() { return content; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
