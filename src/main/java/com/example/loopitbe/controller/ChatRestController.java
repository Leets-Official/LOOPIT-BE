package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.ChatRoomCreateRequest;
import com.example.loopitbe.dto.response.ChatMessageResponse;
import com.example.loopitbe.dto.response.ChatRoomDetailResponse;
import com.example.loopitbe.dto.response.ChatRoomListResponse;
import com.example.loopitbe.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Chat-REST", description = "채팅방 생성, 목록 조회 및 내역 관리 API")
@RestController
@RequestMapping("/chat")
public class ChatRestController {
    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 채팅방 생성 (또는 기존 방 반환)
    @Operation(
            summary = "채팅방 생성 및 연결",
            description = "채팅방을 생성하거나, 이미 존재하는 경우 기존 방 정보를 return"
    )
    @PostMapping("/room/{postId}")
    public ResponseEntity<ApiResponse<ChatRoomDetailResponse>> createRoom(
            @PathVariable Long postId,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId) {
        ChatRoomDetailResponse response = chatService.createOrGetChatRoom(userId, postId);
        return ResponseEntity.ok(ApiResponse.ok(response, "채팅방 연결 성공."));
    }

    // 채팅방 목록 조회
    @Operation(
            summary = "채팅방 목록 조회",
            description = "특정 유저가 참여 중인 모든 채팅방 목록을 조회"
    )
    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoomListResponse>>> getMyRooms(@Parameter(hidden = true) @AuthenticationPrincipal Long userId) {
        List<ChatRoomListResponse> responses = chatService.getMyChatRooms(userId);
        return ResponseEntity.ok(ApiResponse.ok(responses, "채팅 목록 조회 성공."));
    }

    // 이전 채팅 내역 확인
    @Operation(
            summary = "채팅 내역 조회",
            description = "특정 채팅방의 이전 메시지 내역을 조회하며, 해당 유저의 안 읽은 메시지들을 모두 읽음 처리"
    )
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatMessages(
            @PathVariable Long roomId,
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    ) {
        // 해당 유저가 안 읽은 메시지들을 읽음 처리
        chatService.markMessagesAsRead(roomId, userId);

        // 메시지 내역 조회
        List<ChatMessageResponse> responses = chatService.getChatMessages(roomId);
        return ResponseEntity.ok(ApiResponse.ok(responses, "채팅 내역 조회 및 읽음 처리 완료."));
    }

    @Operation(
            summary = "읽지 않은 채팅 존재 여부 조회(네비게이션 바 알림 용)",
            description = "Boolean값으로 반환, 로그인 된 사용자에 대해 읽지 않은 채팅 존재 시 true, 없으면 false"
    )
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkNewMessages(@Parameter(hidden = true) @AuthenticationPrincipal Long userId) {
        // 현재 로그인한 사용자의 ID를 넘겨 읽지 않은 메시지 존재 여부 확인
        boolean hasNew = chatService.existsUnreadMessages(userId);
        return ResponseEntity.ok(ApiResponse.ok(hasNew, "안읽은 채팅 존재 여부 조회 완료."));
    }
}
