package com.yuvaintern.taskmanagement.service;

import com.yuvaintern.taskmanagement.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalDataServiceTest {
    private RestTemplate restTemplate;
    private ExternalDataService service;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        service = new ExternalDataService(restTemplate, "https://dummyjson.com");
    }

    @Test
    void returnsProductsWhenApiSucceeds() {
        ExternalProduct product = new ExternalProduct(1L, "Phone", "Test phone", 99.0, "mobile", "image");
        when(restTemplate.getForObject("https://dummyjson.com/products?limit=5&skip=0",
            ExternalProductsResponse.class))
            .thenReturn(new ExternalProductsResponse(List.of(product), 1, 0, 5));

        ExternalProductsResponse result = service.getProducts(5, 0);

        assertEquals(1, result.products().size());
        assertEquals("Phone", result.products().get(0).title());
    }

    @Test
    void rejectsInvalidPaginationParameters() {
        assertThrows(ResponseStatusException.class, () -> service.getProducts(0, 0));
        assertThrows(ResponseStatusException.class, () -> service.getProducts(5, -1));
        verifyNoInteractions(restTemplate);
    }

    @Test
    void translatesExternalApiFailure() {
        when(restTemplate.getForObject(anyString(), eq(ExternalProductsResponse.class)))
            .thenThrow(new RestClientException("provider unavailable"));
        assertThrows(ResponseStatusException.class, () -> service.getProducts(5, 0));
    }

    @Test
    void rejectsEmptyProviderResponse() {
        when(restTemplate.getForObject(anyString(), eq(ExternalProductsResponse.class))).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> service.getProducts(5, 0));
    }
}
