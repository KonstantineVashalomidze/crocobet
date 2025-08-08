package com.crocobet.konstantinevashalomidze.service;


import com.crocobet.konstantinevashalomidze.model.HealthCheckResult;
import com.crocobet.konstantinevashalomidze.model.MonitoredEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


@Service
public class DynamicHealthCheckService {
    private static final Logger logger = LoggerFactory.getLogger(DynamicHealthCheckService.class);

    private final RestTemplate restTemplate;

    public DynamicHealthCheckService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HealthCheckResult checkEndpoint(MonitoredEndpoint endpoint) {
        HealthCheckResult result = new HealthCheckResult(
                endpoint.getId(),
                endpoint.getName(),
                endpoint.getUrl(),
                endpoint.getMethod()
                );

        long startTime = System.currentTimeMillis();

        // Check if healthy
        try {
            HttpHeaders headers = new HttpHeaders();

            if (endpoint.getHeaders() != null) {
                endpoint.getHeaders().forEach(headers::add);
            }

            HttpEntity<String> requestEntity = new HttpEntity<>(endpoint.getRequestBody(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    endpoint.getUrl(),
                    org.springframework.http.HttpMethod.valueOf(endpoint.getMethod().name()),
                    requestEntity,
                    String.class
            );

            long responseTime = System.currentTimeMillis() - startTime;

            result.setResponseTimeMs(responseTime);
            result.setStatusCode(responseEntity.getStatusCode().value());
            result.setResponseBody(responseEntity.getBody());

            // Checking if status code matches expected one
            boolean statusCodeValid = isStatusCodeValid(responseEntity.getStatusCode(), endpoint.getExpectedStatusCode());

            boolean contentIsValid = isContentValid(responseEntity.getBody(), endpoint.getExpectedContent(), result);


            result.setHealthy(statusCodeValid && contentIsValid);


            logger.info("Health check completed for {}: {} - {}ms - Status: {}", endpoint.getName(), responseEntity.getStatusCode(), responseTime, result.isHealthy() ? "HEALTHY" : "UNHEALTHY");
        } catch (RestClientException ex) {
            long responseTime = System.currentTimeMillis() - startTime;
            result.setResponseTimeMs(responseTime);
            result.setHealthy(false);
            result.setErrorMessage(ex.getMessage());
            result.setStatusCode(0);

            logger.info("Health check failed for {}: {}", endpoint.getName(), ex.getMessage());
        }

        return result;
    }





    private boolean isContentValid(String body, String expectedContent, HealthCheckResult result) {
        if (expectedContent == null || expectedContent.trim().isEmpty()) {
            result.setContentValidationPassed(true);
            return true;
        }

        if (body == null) {
            result.setContentValidationPassed(false);
            result.setValidationMessage("Response body is null.");
            return false;
        }

        boolean contentValid = body.contains(expectedContent);
        result.setContentValidationPassed(true);

        if (!contentValid) {
            result.setValidationMessage("Expected content '" + expectedContent + "' not found in response.");
        } else {
            result.setValidationMessage("Content validation passed.");
        }

        return true;
    }

    private boolean isStatusCodeValid(HttpStatusCode statusCode, String expectedStatusCode) {
        if (expectedStatusCode == null || expectedStatusCode.trim().isEmpty()) {
            return statusCode.is2xxSuccessful();
        }

        String[] expectedCodes = expectedStatusCode.split(",");
        return Arrays.stream(expectedCodes)
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .anyMatch(code -> code == statusCode.value());

    }


}

