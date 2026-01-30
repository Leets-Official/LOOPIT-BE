package com.example.loopitbe.dto.request;

import com.example.loopitbe.enums.PriceRange;

import java.util.List;

public class SellPostSearchCondition {
    private Boolean onlySale;
    private String manufacturer;
    private List<String> series;
    private PriceRange priceRange;

    public SellPostSearchCondition() {}

    public SellPostSearchCondition(Boolean onlySale, String manufacturer, List<String> series, PriceRange priceRange) {
        this.onlySale = onlySale;
        this.manufacturer = manufacturer;
        this.series = series;
        this.priceRange = priceRange;
    }

    // Getters
    public Boolean getOnlySale() { return onlySale; }
    public String getManufacturer() { return manufacturer; }
    public List<String> getSeries() { return series; }
    public PriceRange getPriceRange() { return priceRange; }
}
