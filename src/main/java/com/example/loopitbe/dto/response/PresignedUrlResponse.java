package com.example.loopitbe.dto.response;

public class PresignedUrlResponse {
    private String fileName; // 프론트 매칭용
    private String presignedUrl; // 업로드용 URL
    private String fileUrl;      // S3에 저장될 최종 경로

    public PresignedUrlResponse() {}

    public PresignedUrlResponse(String fileName, String presignedUrl) {
        this.fileName = fileName;
        this.presignedUrl = presignedUrl;
        this.fileUrl = presignedUrl.split("\\?")[0];
    }

    public String getFileName() { return fileName; }
    public String getPresignedUrl() { return presignedUrl; }
    public String getFileUrl() { return fileUrl; }
}
