package com.crocobet.konstantinevashalomidze.model;


import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HealthCheckResult that = (HealthCheckResult) o;
        return isHealthy == that.isHealthy && responseTimeMs == that.responseTimeMs && statusCode == that.statusCode && contentValidationPassed == that.contentValidationPassed && Objects.equals(endpointId, that.endpointId) && Objects.equals(endpointName, that.endpointName) && Objects.equals(url, that.url) && method == that.method && Objects.equals(errorMessage, that.errorMessage) && Objects.equals(timestamp, that.timestamp) && Objects.equals(responseBody, that.responseBody) && Objects.equals(validationMessage, that.validationMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpointId, endpointName, url, method, isHealthy, responseTimeMs, statusCode, errorMessage, timestamp, responseBody, contentValidationPassed, validationMessage);
    }

    @Override
    public String toString() {
        return "HealthCheckResult{" +
                "endpointId='" + endpointId + '\'' +
                ", endpointName='" + endpointName + '\'' +
                ", url='" + url + '\'' +
                ", method=" + method +
                ", isHealthy=" + isHealthy +
                ", responseTimeMs=" + responseTimeMs +
                ", statusCode=" + statusCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", timestamp=" + timestamp +
                ", responseBody='" + responseBody + '\'' +
                ", contentValidationPassed=" + contentValidationPassed +
                ", validationMessage='" + validationMessage + '\'' +
                '}';
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public void setHealthy(boolean healthy) {
        isHealthy = healthy;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public boolean isContentValidationPassed() {
        return contentValidationPassed;
    }

    public void setContentValidationPassed(boolean contentValidationPassed) {
        this.contentValidationPassed = contentValidationPassed;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
}
