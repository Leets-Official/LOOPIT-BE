package com.example.loopitbe.service;

import com.example.loopitbe.entity.Device;
import com.example.loopitbe.repository.DeviceRepository;
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
    public List<String> getSellAutocomplete(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        return repository.findTop10ByModelContainingOrderByModelIdAsc(keyword)
                .stream()
                .map(Device::getModel)
                .toList();
    }

    @Transactional
    public List<String> getBuyAutocomplete(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        return repository.findTop5ByModelContainingOrderByModelIdAsc(keyword)
                .stream()
                .map(Device::getModel)
                .toList();
    }
}
