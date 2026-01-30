package com.example.loopitbe.dto.request;

import com.example.loopitbe.enums.ImageDomain;

import java.util.List;

public class PresignedUrlListRequest {
    private ImageDomain domain;
    private List<String> fileNames;

    public PresignedUrlListRequest() {}

    public PresignedUrlListRequest(ImageDomain domain, List<String> fileNames) {
        this.domain = domain;
        this.fileNames = fileNames;
    }

    // Getters
    public ImageDomain getDomain() { return domain; }
    public List<String> getFileNames() { return fileNames; }
}
