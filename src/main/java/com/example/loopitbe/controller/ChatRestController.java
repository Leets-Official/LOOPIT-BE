package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.request.ChatRoomCreateRequest;
import com.example.loopitbe.dto.response.ChatMessageResponse;
import com.example.loopitbe.dto.response.ChatRoomDetailResponse;
import com.example.loopitbe.dto.response.ChatRoomListResponse;
import com.example.loopitbe.dto.response.PresignedUrlResponse;
import com.example.loopitbe.service.ChatService;
import com.example.loopitbe.service.S3Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "chat-rest", description = "채팅방 관련 Rest Controller")
@RestController
@RequestMapping("/chat")
public class ChatRestController {
    private final ChatService chatService;
    private final S3Service s3Service;

    public ChatRestController(ChatService chatService, S3Service s3Service) {
        this.chatService = chatService;
        this.s3Service = s3Service;
    }

    // 채팅방 생성 (또는 기존 방 반환)
    @PostMapping("/room")
    public ResponseEntity<ApiResponse<ChatRoomDetailResponse>> createRoom(@RequestBody ChatRoomCreateRequest request) {
        ChatRoomDetailResponse response = chatService.createOrGetChatRoom(request);
        return ResponseEntity.ok(ApiResponse.ok(response, "채팅방 연결 성공."));
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoomListResponse>>> getMyRooms(@RequestParam Long userId) {
        List<ChatRoomListResponse> responses = chatService.getMyChatRooms(userId);
        return ResponseEntity.ok(ApiResponse.ok(responses, "채팅 목록 조회 성공."));
    }

    // 이미지 저장
    @GetMapping("/presigned-url")
    public ResponseEntity<ApiResponse<PresignedUrlResponse>> getPresignedUrl(
            @RequestParam String fileName
    ) {
        // "chats" 폴더로 지정
        // 만약 방별로 더 쪼개려면 "chats/" + roomId 로 전달하기. roomId는 param으로 받아오기
        String presignedUrl = s3Service.getPresignedUrl("chats", fileName);

        // 파일 경로 추출
        String fileUrl = presignedUrl.split("\\?")[0];

        PresignedUrlResponse response = new PresignedUrlResponse(presignedUrl, fileUrl);

        return ResponseEntity.ok(ApiResponse.ok(response, "Presigned URL 발급 성공."));
    }

    // 이전 채팅 내역 확인
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatMessages(
            @PathVariable Long roomId,
            @RequestParam Long userId
    ) {
        // 해당 유저가 안 읽은 메시지들을 읽음 처리
        chatService.markMessagesAsRead(roomId, userId);

        // 메시지 내역 조회
        List<ChatMessageResponse> responses = chatService.getChatMessages(roomId);
        return ResponseEntity.ok(ApiResponse.ok(responses, "채팅 내역 조회 및 읽음 처리 완료."));
    }
}
