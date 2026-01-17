package com.example.loopitbe.service;

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

    /**
     * Presigned URL 생성
     * @param prefix 저장할 폴더 경로 (예: "chats", "products")
     * @param fileName 원본 파일명
     * @return 생성된 Presigned URL 문자열
     */
    public String getPresignedUrl(String prefix, String fileName) {
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }

        // 1. 저장될 파일 경로 생성
        String fileNameWithUuid = UUID.randomUUID() + "_" + fileName;
        String key = prefix + fileNameWithUuid;

        // 2. PutObjectRequest 생성 (업로드할 파일 정보)
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        // 3. Presign 요청 생성 (유효기간 설정)
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // 10분간 유효
                .putObjectRequest(objectRequest)
                .build();

        // 4. URL 발급
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        return presignedRequest.url().toString();
    }
}