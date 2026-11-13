package com.yuvaintern.taskmanagement.controller;

import com.yuvaintern.taskmanagement.dto.ExternalProductsResponse;
import com.yuvaintern.taskmanagement.service.ExternalDataService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external")
public class ExternalDataController {
    private final ExternalDataService externalDataService;
    public ExternalDataController(ExternalDataService externalDataService) {
        this.externalDataService = externalDataService;
    }

    @GetMapping("/products")
    public ExternalProductsResponse getProducts(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int skip) {
        return externalDataService.getProducts(limit, skip);
    }
}
