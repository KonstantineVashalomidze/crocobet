package com.crocobet.konstantinevashalomidze.crocobet.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class MonitoredEndpoint {
    private String id;
    private String name;
    private String url;
    private HttpMethod method;
    private Map<String, String> headers;
    private String requestBody;
    private int checkIntervalSeconds;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String expectedStatusCode; // For example "201" and so on...
    private String expectedContent; // Optional content to validate

    public MonitoredEndpoint() {
        id = UUID.randomUUID().toString();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        enabled = true;
        checkIntervalSeconds = 30;
        method = HttpMethod.GET;
    }

    public MonitoredEndpoint(String name, String url, HttpMethod method) {
        this();
        this.name = name;
        this.url = url;
        this.method = method;
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = LocalDateTime.now();
    }


    public void setUrl(String url) {
        this.url = url;
        this.updatedAt = LocalDateTime.now();
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
        this.updatedAt = LocalDateTime.now();
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
        this.updatedAt = LocalDateTime.now();
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCheckIntervalSeconds(int checkIntervalSeconds) {
        this.checkIntervalSeconds = checkIntervalSeconds;
        this.updatedAt = LocalDateTime.now();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.updatedAt = LocalDateTime.now();
    }

    public void setExpectedStatusCode(String expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
        this.updatedAt = LocalDateTime.now();
    }

    public void setExpectedContent(String expectedContent) {
        this.expectedContent = expectedContent;
        this.updatedAt = LocalDateTime.now();
    }

}
