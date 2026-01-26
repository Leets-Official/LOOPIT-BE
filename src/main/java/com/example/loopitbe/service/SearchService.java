package com.example.loopitbe.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Transactional
    public List<String> getAutocomplete(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        return shopRepository.findTop10ByShopNameStartingWith(keyword)
                .stream()
                .map(Shop::getShopName)
                .toList();
    }
}
