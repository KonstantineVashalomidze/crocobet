package com.crocobet.konstantinevashalomidze.crocobet.service;

import com.crocobet.konstantinevashalomidze.crocobet.model.MonitoredEndpoint;
import com.crocobet.konstantinevashalomidze.crocobet.repository.EndpointRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EndpointManagementService {

    private final EndpointRepository endpointRepository;

    private final DynamicMonitoringService monitoringService;

    public EndpointManagementService(EndpointRepository endpointRepository, DynamicMonitoringService monitoringService) {
        this.endpointRepository = endpointRepository;
        this.monitoringService = monitoringService;
    }

    public MonitoredEndpoint createEndpoint(MonitoredEndpoint endpoint) {
        return endpointRepository.save(endpoint);
    }

    public Optional<MonitoredEndpoint> getEndpoint(String id) {
        return endpointRepository.findById(id);
    }

    public List<MonitoredEndpoint> getAllEndpoints() {
        return endpointRepository.findAll();
    }

    public List<MonitoredEndpoint> getEnabledEndpoints() {
        return endpointRepository.findAllEnabled();
    }

    public MonitoredEndpoint updateEndpoint(String id, MonitoredEndpoint updatedEndpoint) {
        return endpointRepository.findById(id)
                .map(existing -> {
                    if (updatedEndpoint.getName() != null) existing.setName(updatedEndpoint.getName());
                    if (updatedEndpoint.getUrl() != null) existing.setUrl(updatedEndpoint.getUrl());
                    if (updatedEndpoint.getMethod() != null) existing.setMethod(updatedEndpoint.getMethod());
                    if (updatedEndpoint.getHeaders() != null) existing.setHeaders(updatedEndpoint.getHeaders());
                    if (updatedEndpoint.getRequestBody() != null) existing.setRequestBody(updatedEndpoint.getRequestBody());
                    if (updatedEndpoint.getCheckIntervalSeconds() > 0) existing.setCheckIntervalSeconds(updatedEndpoint.getCheckIntervalSeconds());
                    if (updatedEndpoint.getExpectedStatusCode() != null) existing.setExpectedStatusCode(updatedEndpoint.getExpectedStatusCode());
                    if (updatedEndpoint.getExpectedContent() != null) existing.setExpectedContent(updatedEndpoint.getExpectedContent());
                    existing.setEnabled(updatedEndpoint.isEnabled());

                    return endpointRepository.save(existing);
                })
                .orElse(null);
    }

    public boolean deleteEndpoint(String id) {
        if (endpointRepository.existsById(id)) {
            endpointRepository.deleteById(id);
            monitoringService.removeEndpointMetrics(id);
            return true;
        }
        return false;
    }

    public MonitoredEndpoint toggleEndpoint(String id) {
        return endpointRepository.findById(id)
                .map(endpoint -> {
                    endpoint.setEnabled(!endpoint.isEnabled());
                    return endpointRepository.save(endpoint);
                })
                .orElse(null);
    }
}