package com.example.loopitbe.service;

import com.example.loopitbe.dto.response.PresignedUrlResponse;
import com.example.loopitbe.enums.ImageDomain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;

import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Service(S3Presigner s3Presigner, S3Client s3Client) {
        this.s3Presigner = s3Presigner;
        this.s3Client = s3Client;
    }

    /**
     * 이미지 삭제 로직 (key)
     */
    public void deleteFile(String key) {
        if (key == null || key.isBlank()) {
            return;
        }

        try {
            System.out.println("S3 파일 삭제 시도! 대상 Key: " + key);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            System.out.println("S3 파일 삭제 성공!");

        } catch (Exception e) {
            // 2. 예외 발생 시 로그를 남기고 우리가 정의한 CustomException을 던집니다.
            System.err.println("S3 삭제 중 예외 발생: " + e.getMessage());
            throw new CustomException(ErrorCode.S3_FILE_DELETE_FAILED);
        }
    }

    public PresignedUrlResponse generatePresignedUrl(ImageDomain domain, String fileName) {
        String presignedUrl = getPresignedUrl(domain.getPrefix(), fileName);
        return new PresignedUrlResponse(presignedUrl);
    }

    public String getPresignedUrl(String prefix, String fileName) {
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