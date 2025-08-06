package com.crocobet.konstantinevashalomidze.crocobet.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class HealthCheckResult {
    private String endpoint;
    private boolean isHealthy;
    private long responseTimeMs;
    private HttpStatus statusCode;
    private String errorMessage;
    private LocalDateTime timestamp;
    private String responseBody;

    public HealthCheckResult() {
        timestamp = LocalDateTime.now();
    }

    public HealthCheckResult(String endpoint, boolean isHealthy, long responseTimeMs, HttpStatus statusCode, String errorMessage, String responseBody) {
        this();
        this.endpoint = endpoint;
        this.isHealthy = isHealthy;
        this.responseTimeMs = responseTimeMs;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.responseBody = responseBody;
    }

}
