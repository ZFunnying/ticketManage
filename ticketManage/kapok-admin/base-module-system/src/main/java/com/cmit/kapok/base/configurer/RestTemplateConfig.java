package com.cmit.kapok.base.configurer;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder
            .setReadTimeout(Duration.ofSeconds(20))
            .setConnectTimeout(Duration.ofSeconds(10))
            .build();
    }
}
