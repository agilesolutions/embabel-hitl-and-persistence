package com.agilesolutions.embabel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    RestClient twelveDataClient() {

        return RestClient.builder()
                .baseUrl("https://api.twelvedata.com")
                .build();
    }
}