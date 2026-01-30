package com.example.loopitbe.enums;

public enum PostStatus {
    SALE("판매중"),
    RESERVED("예약중"),
    COMPLETED("판매완료");

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
