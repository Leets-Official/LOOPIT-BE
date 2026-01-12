package com.example.loopitbe.dto;

import java.time.LocalDateTime;

public record SellPostResponseDto(
        Long id,
        String title,
        String status,
        LocalDateTime createdAt
) {}