package com.example.loopitbe.dto.request;

import com.example.loopitbe.enums.PriceRange;

import java.util.List;

public class SellPostSearchCondition {
    private Boolean onlySale;
    private String manufacturer;
    private List<String> series;
    private PriceRange priceRange;
    private String keyword;          // 검색창에서 사용자가 입력한 키워드

    public SellPostSearchCondition() {}

    public SellPostSearchCondition(Boolean onlySale, String manufacturer, List<String> series, PriceRange priceRange, String keyword) {
        this.onlySale = onlySale;
        this.manufacturer = manufacturer;
        this.series = series;
        this.priceRange = priceRange;
        this.keyword = keyword;
    }

    // Getters
    public Boolean getOnlySale() { return onlySale; }
    public String getManufacturer() { return manufacturer; }
    public List<String> getSeries() { return series; }
    public PriceRange getPriceRange() { return priceRange; }
    public String getKeyword() { return keyword; }

    public void setOnlySale(Boolean onlySale) {
        this.onlySale = onlySale;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setSeries(List<String> series) {
        this.series = series;
    }

    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
