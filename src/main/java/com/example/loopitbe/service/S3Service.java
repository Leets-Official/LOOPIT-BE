package com.example.loopitbe.service;

import com.example.loopitbe.dto.request.PresignedUrlListRequest;
import com.example.loopitbe.dto.response.PresignedUrlResponse;
import com.example.loopitbe.enums.ImageDomain;
import com.example.loopitbe.exception.CustomException;
import com.example.loopitbe.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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

    // 단일 발급
    public PresignedUrlResponse generatePresignedUrl(ImageDomain domain, String fileName) {
        String presignedUrl = createPresignedUrl(domain.getPrefix(), fileName);
        return new PresignedUrlResponse(fileName, presignedUrl);
    }

    // 일괄 발급
    public List<PresignedUrlResponse> generatePresignedUrls(PresignedUrlListRequest request) {
        List<String> fileNames = request.getFileNames();

        if (fileNames == null || fileNames.isEmpty()) {
            return List.of();
        }
        if (fileNames.size() > 5) {
            throw new CustomException(ErrorCode.IMAGE_COUNT_EXCEEDED);
        }

        List<PresignedUrlResponse> responses = new ArrayList<>();
        for (String fileName : fileNames) {
            String presignedUrl = createPresignedUrl(request.getDomain().getPrefix(), fileName);
            responses.add(new PresignedUrlResponse(fileName, presignedUrl));
        }

        return responses;
    }

    // aws sdk 호출 로직
    public String createPresignedUrl(String prefix, String fileName) {
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

    // 이미지 삭제
    public void deleteImage(String fileName) {
        if (fileName == null || fileName.isBlank()) return;

        try {
            // URL에서 Key 추출 https://도메인/CHAT/uuid_file.png -> CHAT/uuid_file.png
            String path = new java.net.URL(fileName).getPath();
            String key = path.startsWith("/") ? path.substring(1) : path;

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.IMAGE_DELETE_FAILED);
        }
    }
}