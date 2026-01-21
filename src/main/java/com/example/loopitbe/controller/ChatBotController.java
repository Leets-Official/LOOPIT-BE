package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.ChatBotRequest;
import com.example.loopitbe.dto.response.ChatBotResponse;
import com.example.loopitbe.service.ChatBotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
public class ChatBotController {
    private final ChatBotService chatBotService;

    public ChatBotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<ChatBotResponse>> sendMessage(@RequestBody ChatBotRequest request) {
        // 1차필터링 -> Redis 제한 체크
        chatBotService.checkAvailability(request.getUserId());

        // Gemini API 호출
        String reply = chatBotService.getRepairEstimate(request.getUserId(), request.getMessage());

        return ResponseEntity.ok(ApiResponse.ok(new ChatBotResponse(reply), "챗봇 답변 완료."));
    }
}