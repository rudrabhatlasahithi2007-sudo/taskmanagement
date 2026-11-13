package com.yuvaintern.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalProduct(Long id, String title, String description, Double price,
                              String category, String thumbnail) {}
