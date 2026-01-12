package com.example.loopitbe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String getPresignedUrl(String fileName) {
        String key = "products/" + UUID.randomUUID() + "_" + fileName;

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