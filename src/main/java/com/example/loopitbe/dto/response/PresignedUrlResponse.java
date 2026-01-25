package com.example.loopitbe.dto.response;

public class PresignedUrlResponse {
    private String presignedUrl; // 업로드용 URL
    private String fileUrl;      // S3에 저장될 최종 경로 (DB 저장용 아님, 프론트 참조용)

    public PresignedUrlResponse() {}

    public PresignedUrlResponse(String presignedUrl) {
        this.presignedUrl = presignedUrl;
        this.fileUrl = presignedUrl.split("\\?")[0];
    }

    public String getPresignedUrl() { return presignedUrl; }
    public String getFileUrl() { return fileUrl; }
}
