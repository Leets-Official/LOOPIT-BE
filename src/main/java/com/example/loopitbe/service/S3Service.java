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
  
    public PresignedUrlResponse generatePresignedUrl(
            ImageDomain domain,
            String fileName
    ) {
        String presignedUrl = getPresignedUrl(domain.getPrefix(), fileName);
        return new PresignedUrlResponse(presignedUrl);
    }

    private String getPresignedUrl(String prefix, String fileName) {
        if (prefix != null && !prefix.endsWith("/")) {
            prefix += "/";
        } else if (prefix == null) {
            prefix = "";
        }

        // 1. 저장될 파일 경로 생성 (UUID 중복 방지)
        String fileNameWithUuid = UUID.randomUUID() + "_" + fileName;
        String key = prefix + fileNameWithUuid;

        // 2. PutObjectRequest 생성
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        // 3. Presign 요청 생성 (유효기간 10분)
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        // 4. URL 발급
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        return presignedRequest.url().toString();
    }
}