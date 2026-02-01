package com.example.loopitbe.dto.request;

import software.amazon.awssdk.annotations.NotNull;

public class UserImgUpdateRequest {
    // null 혹은 공백이면 기존 프로필 이미지 삭제(기본 프로필 적용)
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }
}
