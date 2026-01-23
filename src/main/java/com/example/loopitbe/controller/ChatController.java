package com.example.loopitbe.controller;

import com.example.loopitbe.dto.request.ChatMessageRequest;
import com.example.loopitbe.dto.request.ChatReadRequest;
import com.example.loopitbe.dto.response.ChatMessageResponse;
import com.example.loopitbe.dto.response.ChatReadEventResponse;
import com.example.loopitbe.redis.RedisPublisher;
import com.example.loopitbe.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Tag(name = "chat", description = "WebSocket 연결 관련")
@Controller // 관례상 RestController가 아닌 Controller 어노테이션 사용
public class ChatController {

    private final ChatService chatService;
    private final RedisPublisher redisPublisher;

    private final ChannelTopic topicChat = new ChannelTopic("chat:message");
    private final ChannelTopic topicRead = new ChannelTopic("chat:read");

    public ChatController(
            ChatService chatService,
            RedisPublisher redisPublisher
    ) {
        this.chatService = chatService;
        this.redisPublisher = redisPublisher;
    }

    /*
     * 클라이언트가 /pub/chat/message로 메시지를 보내면 호출
     * WebSocketConfig에서 설정한 destinationPrefixes와 합쳐집니다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageRequest request) {
        // 1. 메시지 저장 및 보낸 사람 정보가 포함된 응답 객체 생성
        ChatMessageResponse response = chatService.saveMessage(request);

        // 2. Redis 발행
        redisPublisher.publish(topicChat, response);
    }

    @MessageMapping("/chat/read")
    public void readMessage(ChatReadRequest request) {
        // 1. DB 업데이트 (메시지 엔티티들의 isRead 상태만 변경)
        chatService.markMessagesAsRead(request.getRoomId(), request.getUserId());

        // 2. 실시간 알림 전송
        ChatReadEventResponse readEvent = new ChatReadEventResponse(request.getRoomId(), request.getUserId());

        // 3. Redis 발행
        redisPublisher.publish(topicRead, readEvent);
    }
}
