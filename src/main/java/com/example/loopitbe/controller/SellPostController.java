package com.example.loopitbe.controller;

import com.example.loopitbe.dto.SellPostRequestDto;
import com.example.loopitbe.dto.SellPostResponseDto;
import com.example.loopitbe.entity.SellPost;
import com.example.loopitbe.entity.User;
import com.example.loopitbe.repository.SellPostRepository;
import com.example.loopitbe.repository.UserRepository;
import com.example.loopitbe.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sell-posts")
@RequiredArgsConstructor
public class SellPostController {

    private final S3Service s3Service;
    private final SellPostRepository sellPostRepository;
    private final UserRepository userRepository;

    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam String fileName) {
        return ResponseEntity.ok(s3Service.getPresignedUrl(fileName));
    }

    @PostMapping
    public ResponseEntity<SellPostResponseDto> createSellPost(
            @RequestBody SellPostRequestDto requestDto,
            Principal principal) {

        String kakaoId = principal.getName();
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        SellPost sellPost = SellPost.builder()
                .user(user)
                .title(requestDto.title())
                .content(requestDto.description())
                .price(requestDto.price())
                .model(requestDto.modelName())
                .manufacturer(requestDto.manufacturer())
                .color(requestDto.color())
                .capacity(requestDto.capacity())
                .components(requestDto.components())
                .imageUrls(requestDto.imageUrls())
                .build();

        SellPost savedPost = sellPostRepository.save(sellPost);

        SellPostResponseDto response = new SellPostResponseDto(
                savedPost.getId(),
                savedPost.getTitle(),
                "판매글이 성공적으로 등록되었습니다.",
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}