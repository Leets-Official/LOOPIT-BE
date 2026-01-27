package com.example.loopitbe.dto.request;

import java.util.List;

public class SellPostRequest {
    private String title;
    private String manufacturer;
    private String modelName;
    private String color;
    private String capacity;
    private Long price;
    private String description;
    private List<String> components;
    private List<String> imageUrls;

    public SellPostRequest() {}

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

    public String getTitle() { return title; }
    public String getManufacturer() { return manufacturer; }
    public String getModelName() { return modelName; }
    public String getColor() { return color; }
    public String getCapacity() { return capacity; }
    public Long getPrice() { return price; }
    public String getDescription() { return description; }
    public List<String> getComponents() { return components; }
    public List<String> getImageUrls() { return imageUrls; }
}