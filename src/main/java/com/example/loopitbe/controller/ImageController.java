package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.response.PresignedUrlResponse;
import com.example.loopitbe.enums.ImageDomain;
import com.example.loopitbe.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Image", description = "S3 이미지 업로드를 위한 Presigned URL 발급 API")
@RestController
@RequestMapping("/image")
public class ImageController {

    private final S3Service s3Service;

    public ImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Operation(
            summary = "Presigned URL 발급",
            description = "S3에 이미지를 직접 업로드하기 위한 URL 발급. 발급받은 URL로 PUT 요청을 보내 이미지를 업로드."
    )
    @GetMapping("/presigned-url")
    public ResponseEntity<ApiResponse<PresignedUrlResponse>> getPresignedUrl(
            @Parameter(description = "이미지가 저장될 도메인 구분 (PRODUCT, CHAT, PROFILE)", required = true)
            @RequestParam ImageDomain domain,
            @Parameter(description = "확장자를 포함한 파일 이름", required = true)
            @RequestParam String fileName
    ) {
        PresignedUrlResponse response =
                s3Service.generatePresignedUrl(domain, fileName);

        return ResponseEntity.ok(
                ApiResponse.ok(response, "Presigned URL 발급 성공.")
        );
    }
}
