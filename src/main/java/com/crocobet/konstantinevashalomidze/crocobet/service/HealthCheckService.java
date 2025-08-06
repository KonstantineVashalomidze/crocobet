package com.crocobet.konstantinevashalomidze.crocobet.service;


import com.crocobet.konstantinevashalomidze.crocobet.model.HealthCheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
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

        long startTime = System.currentTimeMillis();


        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            long responseTime = System.currentTimeMillis() - startTime;

            result.setResponseTimeMs(responseTime);
            result.setStatusCode(response.getStatusCode().value());
            result.setHealthy(response.getStatusCode().is2xxSuccessful());
            result.setResponseBody(response.getBody());

            logger.info("Health check successful for {}: {} - {}ms", url, response.getStatusCode(), responseTime);
        } catch (RestClientException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            result.setResponseTimeMs(responseTime);
            result.setHealthy(false);
            result.setErrorMessage(e.getMessage());
            result.setStatusCode(-1);

            logger.info("Health check unsuccessful for {}: {}", url, e.getMessage());
        }
        return result;
    }


}
