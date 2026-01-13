package com.example.loopitbe.controller;

import com.example.loopitbe.dto.request.ChatMessageRequest;
import com.example.loopitbe.dto.response.ChatMessageResponse;
import com.example.loopitbe.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Tag(name = "chat", description = "채팅방 관련")
@Controller // 관례상 RestController가 아닌 Controller 어노테이션 사용
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessageSendingOperations messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    /*
     * 클라이언트가 /pub/chat/message로 메시지를 보내면 호출됩니다.
     * WebSocketConfig에서 설정한 destinationPrefixes와 합쳐집니다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageRequest request) {
        // 1. 메시지 저장 및 보낸 사람 정보가 포함된 응답 객체 생성
        ChatMessageResponse response = chatService.saveMessage(request);

        // 2. /sub/chat/room/{roomId}를 구독하고 있는 사용자들에게 메시지 전달
        // 클라이언트는 이 경로를 구독(Subscribe)하고 있어야 메시지를 실시간으로 받습니다.
        messagingTemplate.convertAndSend("/sub/chat/room/" + response.getRoomId(), response);
    }
}
