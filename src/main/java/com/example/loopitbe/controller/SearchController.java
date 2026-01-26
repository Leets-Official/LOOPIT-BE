package com.example.loopitbe.controller;

import com.example.loopitbe.service.S3Service;
import com.example.loopitbe.service.SearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Search", description = "검색 관련 컨트롤러")
@RestController
@RequestMapping("/search")
public class SearchController {
    private final SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }
}
