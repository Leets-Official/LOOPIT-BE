package com.example.loopitbe.dto;

import java.time.LocalDateTime;

public record SellPostResponse(
        Long id,
        String title,
        String status,
        LocalDateTime createdAt
) {}