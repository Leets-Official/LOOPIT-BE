package com.example.loopitbe.exception;

public class RateLimitException extends RuntimeException {
    private final Long remainingHours;

    public RateLimitException(Long remainingHours) {
        super(remainingHours + "시간 후에 다시 시도해 주세요.");
        this.remainingHours = remainingHours;
    }

    public Long getRemainingHours() {
        return remainingHours;
    }
}
