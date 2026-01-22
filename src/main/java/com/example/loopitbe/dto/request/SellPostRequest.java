package com.example.loopitbe.dto.request;

import java.util.List;

public class SellPostRequest {
    private final String title;
    private final String manufacturer;
    private final String modelName;
    private final String color;
    private final String capacity;
    private final Long price;
    private final String description;
    private final List<String> components;
    private final List<String> imageUrls;

    public SellPostRequest(String title, String manufacturer, String modelName, String color,
                           String capacity, Long price, String description,
                           List<String> components, List<String> imageUrls) {
        this.title = title;
        this.manufacturer = manufacturer;
        this.modelName = modelName;
        this.color = color;
        this.capacity = capacity;
        this.price = price;
        this.description = description;
        this.components = components;
        this.imageUrls = imageUrls;
    }

    public String title() { return title; }
    public String manufacturer() { return manufacturer; }
    public String modelName() { return modelName; }
    public String color() { return color; }
    public String capacity() { return capacity; }
    public Long price() { return price; }
    public String description() { return description; }
    public List<String> components() { return components; }
    public List<String> imageUrls() { return imageUrls; }
}