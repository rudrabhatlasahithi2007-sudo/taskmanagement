package com.yuvaintern.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalProductsResponse(List<ExternalProduct> products, Integer total,
                                        Integer skip, Integer limit) {}
