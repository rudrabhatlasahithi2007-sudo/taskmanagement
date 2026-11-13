package com.yuvaintern.taskmanagement.service;

import com.yuvaintern.taskmanagement.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class ExternalDataService {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ExternalDataService(RestTemplate externalRestTemplate,
            @Value("${external.api.base-url}") String baseUrl) {
        this.restTemplate = externalRestTemplate;
        this.baseUrl = baseUrl;
    }

    public ExternalProductsResponse getProducts(int limit, int skip) {
        if (limit < 1 || limit > 100 || skip < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "limit must be 1..100 and skip must be zero or greater");
        }
        try {
            ExternalProductsResponse response = restTemplate.getForObject(
                baseUrl + "/products?limit=" + limit + "&skip=" + skip,
                ExternalProductsResponse.class);
            if (response == null || response.products() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "External API returned an empty response");
            }
            return response;
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                "Unable to retrieve products from external API", ex);
        }
    }
}
