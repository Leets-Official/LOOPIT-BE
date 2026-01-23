package com.example.loopitbe.redis;

import com.example.loopitbe.dto.response.ChatMessageResponse;
import com.example.loopitbe.dto.response.ChatReadEventResponse;
import com.example.loopitbe.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    public RedisSubscriber(ObjectMapper objectMapper, SimpMessageSendingOperations messagingTemplate) {
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 일반 메시지 수신 (chat:message 토픽)
     * RedisConfig에서 listenerAdapterChat을 통해 이 메서드가 호출됨
     */
    public void sendMessage(String publishMessage) {
        try {
            // 1. JSON 문자열 -> Java DTO 변환
            ChatMessageResponse chatMessage = objectMapper.readValue(publishMessage, ChatMessageResponse.class);
            // 2. WebSocket 구독자들에게 전송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);

        } catch (Exception e) {
            System.out.println("[Redis Error] " + ErrorCode.REDIS_PARSE_ERROR.getMessage());
        }
    }

    /**
     * 읽음 이벤트 수신 (chat:read 토픽)
     * RedisConfig에서 listenerAdapterRead를 통해 이 메서드가 호출됨
     */
    public void sendReadMessage(String publishMessage) {
        try {
            ChatReadEventResponse readEvent = objectMapper.readValue(publishMessage, ChatReadEventResponse.class);
            messagingTemplate.convertAndSend("/sub/chat/room/" + readEvent.getRoomId(), readEvent);

        } catch (Exception e) {
            System.out.println("[Redis Error] " + ErrorCode.REDIS_PARSE_ERROR.getMessage());
        }
    }
}