package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.ChatBotRequest;
import com.example.loopitbe.dto.response.ChatBotHistoryResponse;
import com.example.loopitbe.dto.response.ChatBotResponse;
import com.example.loopitbe.service.ChatBotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ChatBot", description = "챗봇 관련 컨트롤러")
@RestController
@RequestMapping("/chatbot")
public class ChatBotController {
    private final ChatBotService chatBotService;

    public ChatBotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @Operation(
            summary = "챗봇에게 질문 전송",
            description = "첫번째 질문 기준으로 24시간 TTL 설정, 5개 질문 초과 시 남은 TTL과 함께 에러 반환, " +
                    "수리비 견적과 상관없는 질문 시 reply필드에 '제가 답변드릴 수 없는 질문입니다. 저는 수리비를 예측해주는 챗봇입니다.'라고 return"
    )
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<ChatBotResponse>> sendMessage(@RequestBody ChatBotRequest request) {
        // 1차필터링 -> Redis 제한 체크
        chatBotService.checkAvailability(request.getUserId());

        // Gemini API 호출
        String reply = chatBotService.getRepairEstimate(request.getUserId(), request.getMessage());

        return ResponseEntity.ok(ApiResponse.ok(new ChatBotResponse(reply), "챗봇 답변 완료."));
    }

    @Operation(
            summary = "대화 내용 조회",
            description = "userId에 해당하는 TTL 살아있는 모든 질문&답변 조회"
    )
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<ChatBotHistoryResponse>>> getChatBotHistory(@RequestParam("userId") Long userId) {
        List<ChatBotHistoryResponse> history = chatBotService.getChatHistoryParsed(userId);

        return ResponseEntity.ok(ApiResponse.ok(history, "챗봇 대화내역 조회 완료."));
    }
}