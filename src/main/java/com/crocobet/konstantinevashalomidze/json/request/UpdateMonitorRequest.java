package com.crocobet.konstantinevashalomidze.json.request;

import com.crocobet.konstantinevashalomidze.model.HttpMethod;

import java.util.Map;

public record UpdateMonitorRequest(String name, String url, HttpMethod method, Map<String, String> headers,
                                   String requestBody, boolean enabled,
                                   String expectedStatusCode, String expectedContent) {

}
