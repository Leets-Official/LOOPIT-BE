package com.example.loopitbe.controller;

import com.example.loopitbe.common.ApiResponse;
import com.example.loopitbe.service.S3Service;
import com.example.loopitbe.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Search", description = "검색 관련 컨트롤러")
@RestController
@RequestMapping("/search")
public class SearchController {
    private final SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }

    @Operation(
            summary = "기기 검색 Autocomplete",
            description = "Contains 방식으로 앞뒤로 키워드가 포함된 모델명 return"
    )
    @GetMapping("/autocomplete/sell")
    public ResponseEntity<ApiResponse<List<String>>> autocompleteSell(@RequestParam(name = "keyword") String keyword) {
        List<String> results = service.getSellAutocomplete(keyword);
        return ResponseEntity.ok(ApiResponse.ok(results, "Auto complete"));
    }


    @Operation(
            summary = "구매하기 검색창 Autocomplete",
            description = "Contains 방식으로 앞뒤로 키워드가 포함된 모델명 return"
    )
    @GetMapping("/autocomplete/buy")
    public ResponseEntity<ApiResponse<List<String>>> autocompleteBuy(@RequestParam(name = "keyword") String keyword) {
        List<String> results = service.getBuyAutocomplete(keyword);
        return ResponseEntity.ok(ApiResponse.ok(results, "Auto complete"));
    }
}
