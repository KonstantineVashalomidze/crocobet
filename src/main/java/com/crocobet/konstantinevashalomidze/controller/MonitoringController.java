package com.crocobet.konstantinevashalomidze.controller;


import com.crocobet.konstantinevashalomidze.model.HealthCheckResult;
import com.crocobet.konstantinevashalomidze.model.MonitoredEndpoint;
import com.crocobet.konstantinevashalomidze.service.DynamicMonitoringService;
import com.crocobet.konstantinevashalomidze.service.EndpointManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitor")
@CrossOrigin(origins = "*")
public class MonitoringController {

    private final EndpointManagementService endpointService;

    private final DynamicMonitoringService monitoringService;

    public MonitoringController(EndpointManagementService endpointService, DynamicMonitoringService monitoringService) {
        this.endpointService = endpointService;
        this.monitoringService = monitoringService;
    }

    @PostMapping("/endpoints")
    public ResponseEntity<MonitoredEndpoint> createEndpoint(@RequestBody MonitoredEndpoint endpoint) {
        MonitoredEndpoint created = endpointService.createEndpoint(endpoint);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/endpoints")
    public ResponseEntity<List<MonitoredEndpoint>> getAllEndpoints() {
        return new ResponseEntity<>(endpointService.getAllEndpoints(), HttpStatus.OK);
    }

    @GetMapping("/endpoints/enabled")
    public ResponseEntity<List<MonitoredEndpoint>> getEnabledEndpoints() {
        return ResponseEntity.ok(endpointService.getEnabledEndpoints());
    }

    @GetMapping("/endpoints/{id}")
    public ResponseEntity<MonitoredEndpoint> getEndpoint(@PathVariable String id) {
        return endpointService.getEndpoint(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/endpoints/{id}")
    public ResponseEntity<MonitoredEndpoint> updateEndpoint(@PathVariable String id, @RequestBody MonitoredEndpoint endpoint) {
        MonitoredEndpoint updated = endpointService.updateEndpoint(id, endpoint);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/endpoints/{id}")
    public ResponseEntity<Void> deleteEndpoint(@PathVariable String id) {
        return endpointService.deleteEndpoint(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/endpoints/{id}/toggle")
    public ResponseEntity<MonitoredEndpoint> toggleEndpoint(@PathVariable String id) {
        MonitoredEndpoint toggled = endpointService.toggleEndpoint(id);
        return toggled != null ? ResponseEntity.ok(toggled) : ResponseEntity.notFound().build();
    }

    // Health Check Operations
    @PostMapping("/endpoints/{id}/check")
    public ResponseEntity<HealthCheckResult> performHealthCheck(@PathVariable String id) {
        HealthCheckResult result = monitoringService.performSingleHealthCheck(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        long totalEndpoints = endpointService.getAllEndpoints().size();
        long enabledEndpoints = endpointService.getEnabledEndpoints().size();
        return ResponseEntity.ok(String.format("Dynamic Monitor is running! Total endpoints: %d, Enabled: %d",
                totalEndpoints, enabledEndpoints));
    }
}