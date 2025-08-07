package com.crocobet.konstantinevashalomidze.controller;


import com.crocobet.konstantinevashalomidze.json.request.CreateMonitorRequest;
import com.crocobet.konstantinevashalomidze.json.request.UpdateMonitorRequest;
import com.crocobet.konstantinevashalomidze.model.HealthCheckResult;
import com.crocobet.konstantinevashalomidze.model.MonitoredEndpoint;
import com.crocobet.konstantinevashalomidze.service.DynamicMonitoringService;
import com.crocobet.konstantinevashalomidze.service.EndpointManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<MonitoredEndpoint> createEndpoint(@RequestBody CreateMonitorRequest request) {
        MonitoredEndpoint created = endpointService.createEndpoint(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/endpoints")
    public ResponseEntity<List<MonitoredEndpoint>> getAllEndpoints() {
        return new ResponseEntity<>(endpointService.getAllEndpoints(), HttpStatus.OK);
    }

    @GetMapping("/endpoints/enabled")
    public ResponseEntity<List<MonitoredEndpoint>> getEnabledEndpoints() {
        return new ResponseEntity<>(endpointService.getEnabledEndpoints(), HttpStatus.OK);
    }

    @GetMapping("/endpoints/{id}")
    public ResponseEntity<MonitoredEndpoint> getEndpoint(@PathVariable String id) {
        Optional<MonitoredEndpoint> monitoredEndpoint =  endpointService.getEndpoint(id);
        return monitoredEndpoint.map(endpoint -> new ResponseEntity<>(endpoint, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/endpoints/{id}")
    public ResponseEntity<MonitoredEndpoint> updateEndpoint(@PathVariable String id, @RequestBody UpdateMonitorRequest request) {
        Optional<MonitoredEndpoint> updated = endpointService.updateEndpoint(id, request);
        return updated
                .map(monitoredEndpoint ->
                        new ResponseEntity<>(monitoredEndpoint, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
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