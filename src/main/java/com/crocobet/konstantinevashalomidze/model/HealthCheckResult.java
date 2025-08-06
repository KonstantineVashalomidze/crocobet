package com.crocobet.konstantinevashalomidze.model;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class HealthCheckResult {
    private String endpointId;
    private String endpointName;
    private String url;
    private HttpMethod method;
    private boolean isHealthy;
    private long responseTimeMs;
    private int statusCode;
    private String errorMessage;
    private LocalDateTime timestamp;
    private String responseBody;
    private boolean contentValidationPassed;
    private String validationMessage;

    public HealthCheckResult() {
        this.timestamp = LocalDateTime.now();
    }


    public HealthCheckResult(String endpointId, String endpointName, String url, HttpMethod method) {
        this();
        this.endpointId = endpointId;
        this.endpointName = endpointName;
        this.url = url;
        this.method = method;
    }



}
