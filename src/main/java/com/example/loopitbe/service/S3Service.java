package com.example.loopitbe.service;

import com.example.loopitbe.dto.response.PresignedUrlResponse;
import com.example.loopitbe.enums.ImageDomain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Service(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    public PresignedUrlResponse generatePresignedUrl(ImageDomain domain, String fileName) {
        String presignedUrl = getPresignedUrl(domain.getPrefix(), fileName);
        return new PresignedUrlResponse(presignedUrl);
    }

    public String getPresignedUrl(String prefix, String fileName) {
        // 리뷰 반영: prefix 끝에 슬래시(/)가 없으면 자동으로 추가
        if (prefix != null && !prefix.endsWith("/")) {
            prefix += "/";
        } else if (prefix == null) {
            prefix = "";
        }

        // 1. 저장될 파일 경로 생성 (UUID + 원본파일명)
        String fileNameWithUuid = UUID.randomUUID() + "_" + fileName;
        String key = prefix + fileNameWithUuid;

        // 2. S3 업로드 요청 객체 생성
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        // 3. Presigned URL 요청 설정 (유효기간 10분)
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        // 4. 최종 URL 발급 및 반환
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        return presignedRequest.url().toString();
    }
}