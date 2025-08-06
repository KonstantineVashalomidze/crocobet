package com.crocobet.konstantinevashalomidze.model;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MonitoredEndpoint {
    private final String id;
    private String name;
    private String url;
    private HttpMethod method;
    private Map<String, String> headers;
    private String requestBody;
    private int checkIntervalSeconds;
    private boolean enabled;
    private final LocalDateTime createdAt;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MonitoredEndpoint that = (MonitoredEndpoint) o;
        return checkIntervalSeconds == that.checkIntervalSeconds && enabled == that.enabled && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(url, that.url) && method == that.method && Objects.equals(headers, that.headers) && Objects.equals(requestBody, that.requestBody) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(expectedStatusCode, that.expectedStatusCode) && Objects.equals(expectedContent, that.expectedContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, method, headers, requestBody, checkIntervalSeconds, enabled, createdAt, updatedAt, expectedStatusCode, expectedContent);
    }

    @Override
    public String toString() {
        return "MonitoredEndpoint{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", method=" + method +
                ", headers=" + headers +
                ", requestBody='" + requestBody + '\'' +
                ", checkIntervalSeconds=" + checkIntervalSeconds +
                ", enabled=" + enabled +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", expectedStatusCode='" + expectedStatusCode + '\'' +
                ", expectedContent='" + expectedContent + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public int getCheckIntervalSeconds() {
        return checkIntervalSeconds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getExpectedStatusCode() {
        return expectedStatusCode;
    }

    public String getExpectedContent() {
        return expectedContent;
    }
}
