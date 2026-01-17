package com.example.loopitbe.dto;

import java.util.List;

public record SellPostRequest(
        String title,
        String manufacturer,
        String modelName,
        String color,
        String capacity,
        Long price,
        String description,
        List<String> components,
        List<String> imageUrls
) {}