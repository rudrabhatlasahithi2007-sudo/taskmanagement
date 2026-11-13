package com.yuvaintern.taskmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Configuration
public class ExternalApiConfig {
    @Bean
    RestTemplate externalRestTemplate(RestTemplateBuilder builder,
            @Value("${external.api.connect-timeout:5s}") Duration connectTimeout,
            @Value("${external.api.read-timeout:10s}") Duration readTimeout) {
        return builder.connectTimeout(connectTimeout).readTimeout(readTimeout).build();
    }
}
