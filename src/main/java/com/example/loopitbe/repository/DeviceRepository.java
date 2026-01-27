package com.example.loopitbe.repository;

import com.example.loopitbe.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findTop10ByModelContainingOrderByModelIdAsc(String keyword);
}
