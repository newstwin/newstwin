package com.est.newstwin.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class AlanAIConfig {

    private final Environment env;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Alan API 기본 URL
    public String getBaseUrl() {
        return env.getProperty("ALAN_BASE_URL");
    }

    // Alan Client ID
    public String getClientId() {
        return env.getProperty("ALAN_CLIENT_ID");
    }

    @PostConstruct
    public void printEnvValues() {
        System.out.println("[Alan Config] Base URL = " + getBaseUrl());
        System.out.println("[Alan Config] Client ID = " + getClientId());
    }

}
