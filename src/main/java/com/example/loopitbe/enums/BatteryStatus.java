package com.example.loopitbe.enums;

public enum BatteryStatus {
    GREAT("80% 이상"),
    GOOD("80% 미만"),
    BAD("50% 미만");

    private final String description;

    BatteryStatus(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}