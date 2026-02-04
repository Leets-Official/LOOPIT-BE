package com.example.loopitbe.enums;

public enum TransactionStatus {
    RESERVED("예약중"),
    COMPLETED("거래완료"),
    CANCELED("취소됨");    // 예약중, 거래 완료 -> 판매 중 전환 시 기존 거래가 취소된 것으로 설정

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
