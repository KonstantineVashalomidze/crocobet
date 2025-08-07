package com.crocobet.konstantinevashalomidze.service;

import com.crocobet.konstantinevashalomidze.json.request.CreateMonitorRequest;
import com.crocobet.konstantinevashalomidze.json.request.UpdateMonitorRequest;
import com.crocobet.konstantinevashalomidze.model.MonitoredEndpoint;
import com.crocobet.konstantinevashalomidze.repository.EndpointRepository;
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

    public MonitoredEndpoint createEndpoint(CreateMonitorRequest request) {
        return endpointRepository.save(new MonitoredEndpoint(request));
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

    public Optional<MonitoredEndpoint> updateEndpoint(String id, UpdateMonitorRequest request) {
        return endpointRepository.findById(id)
                .map(existing -> {
                    if (request.name() != null) existing.setName(request.name());
                    if (request.url() != null) existing.setUrl(request.url());
                    if (request.method() != null) existing.setMethod(request.method());
                    if (request.headers() != null) existing.setHeaders(request.headers());
                    if (request.requestBody() != null) existing.setRequestBody(request.requestBody());
                    if (request.expectedStatusCode() != null) existing.setExpectedStatusCode(request.expectedStatusCode());
                    if (request.expectedContent() != null) existing.setExpectedContent(request.expectedContent());
                    existing.setEnabled(request.enabled());

                    return endpointRepository.save(existing);
                });
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