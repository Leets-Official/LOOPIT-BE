package com.example.loopitbe.enums;

public enum PriceRange {
    UNDER_10(0L, 100000L),
    FROM_10_TO_30(100000L, 300000L),
    FROM_30_TO_60(300000L, 600000L),
    FROM_60_TO_90(600000L, 900000L),
    OVER_100(1000000L, Long.MAX_VALUE);

    private final Long min;
    private final Long max;

    PriceRange(Long min, Long max) {
        this.min = min;
        this.max = max;
    }

    public Long getMin() { return min; }
    public Long getMax() { return max; }
}
