package com.crocobet.konstantinevashalomidze.crocobet.service;


import com.crocobet.konstantinevashalomidze.crocobet.model.HealthCheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class HealthCheckService {
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);

    private final RestTemplate restTemplate;

    public HealthCheckService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HealthCheckResult checkEndpoint(String url) {
        HealthCheckResult result = new HealthCheckResult();
        result.setEndpoint(url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        }
    }


}
