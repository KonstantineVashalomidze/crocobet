package com.crocobet.konstantinevashalomidze.crocobet.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record HealthCheckResult(String endpoint, boolean isHealthy, long responseTimeMs, HttpStatus statusCode, String errorMessage, LocalDateTime timestamp, String responseBody) {

    public HealthCheckResult {
        timestamp = LocalDateTime.now();
    }

}
