package com.example.loopitbe.entity;

import com.example.loopitbe.enums.DeviceCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "model_id")
    private Long modelId;

    @Column(nullable = false)
    private String manufacturer;    // APPLE, SAMSUNG

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String series;          // 아이폰 17 => 아이폰 17 , 아이폰 17 Pro, 아이폰 17 Pro Max 등...

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceCategory category;

    public Long getModelId() {
        return modelId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getSeries() {
        return series;
    }

    public DeviceCategory getCategory() {
        return category;
    }
}
