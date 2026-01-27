package com.example.loopitbe.service;

import com.example.loopitbe.entity.Device;
import com.example.loopitbe.repository.DeviceRepository;
import com.example.loopitbe.repository.SellPostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    private final DeviceRepository repository;

    public SearchService(DeviceRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<String> getAutocomplete(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        return repository.findTop10ByModelContainingOrderByModelIdAsc(keyword)
                .stream()
                .map(Device::getModel)
                .toList();
    }
}
