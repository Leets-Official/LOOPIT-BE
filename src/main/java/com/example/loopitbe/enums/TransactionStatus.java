package com.example.loopitbe.enums;

public enum TransactionStatus {
    RESERVED("예약중"),
    COMPLETED("거래완료"),
    CANCELED("취소됨");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
