package com.example.loopitbe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

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
     * S3 Presigned URL 생성
     * @param prefix 저장할 폴더 경로 (
     * @param fileName 원본 파일명
     * @return 생성된 Presigned URL 문자열
     */
    public String getPresignedUrl(String prefix, String fileName) {
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }

        String key = prefix + UUID.randomUUID() + "_" + fileName;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(r -> r
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest));

        return presignedRequest.url().toString();
    }
}