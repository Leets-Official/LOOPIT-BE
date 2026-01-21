package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.dto.response.PresignedUrlResponse;
import com.example.loopitbe.enums.ImageDomain;
import com.example.loopitbe.service.S3Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Image", description = "presigned url 발급")
@RestController
@RequestMapping("/image")
public class ImageController {

    private final S3Service s3Service;

    public ImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<ApiResponse<PresignedUrlResponse>> getPresignedUrl(
            @RequestParam ImageDomain domain,
            @RequestParam String fileName
    ) {
        PresignedUrlResponse response =
                s3Service.generatePresignedUrl(domain, fileName);

        return ResponseEntity.ok(
                ApiResponse.ok(response, "Presigned URL 발급 성공.")
        );
    }
}
