package com.example.loopitbe.dto.request;

import com.example.loopitbe.enums.BatteryStatus;

import java.util.List;

public class SellPostRequest {
    private String title;
    private String manufacturer;
    private String model;
    private String color;
    private String capacity;
    private Long price;
    private String description;
    private List<String> imageUrls;
    private boolean used;
    private boolean hasScratch;
    private boolean screenCracked;
    private BatteryStatus batteryStatus;

    public SellPostRequest() {}

    public SellPostRequest(String title, String manufacturer, String model, String color,
                           String capacity, Long price, String description, List<String> imageUrls) {
        this.title = title;
        this.manufacturer = manufacturer;
        this.model = model;
        this.color = color;
        this.capacity = capacity;
        this.price = price;
        this.description = description;
        this.imageUrls = imageUrls;
    }

    public String getTitle() { return title; }
    public String getManufacturer() { return manufacturer; }
    public String getModel() { return model; }
    public String getColor() { return color; }
    public String getCapacity() { return capacity; }
    public Long getPrice() { return price; }
    public String getDescription() { return description; }
    public List<String> getImageUrls() { return imageUrls; }
    public boolean isHasScratch() { return hasScratch; }
    public BatteryStatus getBatteryStatus() { return batteryStatus; }

    public boolean isUsed() {
        return used;
    }

    public boolean isScreenCracked() {
        return screenCracked;
    }
}