package com.crocobet.konstantinevashalomidze.controller;

import com.crocobet.konstantinevashalomidze.json.request.CreateMonitorRequest;
import com.crocobet.konstantinevashalomidze.json.request.UpdateMonitorRequest;
import com.crocobet.konstantinevashalomidze.model.HealthCheckResult;
import com.crocobet.konstantinevashalomidze.model.MonitoredEndpoint;
import com.crocobet.konstantinevashalomidze.service.DynamicMonitoringService;
import com.crocobet.konstantinevashalomidze.service.EndpointManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/monitor")
@CrossOrigin(origins = "*")
@Tag(name = "Monitoring Controller", description = "API for managing endpoint monitoring and health checks")
public class MonitoringController {

    private final EndpointManagementService endpointService;
    private final DynamicMonitoringService monitoringService;

    public MonitoringController(EndpointManagementService endpointService, DynamicMonitoringService monitoringService) {
        this.endpointService = endpointService;
        this.monitoringService = monitoringService;
    }

    @PostMapping("/endpoints")
    @Operation(summary = "Create a new monitored endpoint", description = "Creates a new endpoint to be monitored for health checks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Endpoint created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MonitoredEndpoint.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<MonitoredEndpoint> createEndpoint(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Endpoint creation request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateMonitorRequest.class))
            )
            @RequestBody CreateMonitorRequest request) {
        MonitoredEndpoint created = endpointService.createEndpoint(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/endpoints")
    @Operation(summary = "Get all endpoints", description = "Retrieves all monitored endpoints")
    @ApiResponse(responseCode = "200", description = "List of all endpoints retrieved successfully")
    public ResponseEntity<List<MonitoredEndpoint>> getAllEndpoints() {
        return new ResponseEntity<>(endpointService.getAllEndpoints(), HttpStatus.OK);
    }

    @GetMapping("/endpoints/enabled")
    @Operation(summary = "Get enabled endpoints", description = "Retrieves only enabled monitored endpoints")
    @ApiResponse(responseCode = "200", description = "List of enabled endpoints retrieved successfully")
    public ResponseEntity<List<MonitoredEndpoint>> getEnabledEndpoints() {
        return new ResponseEntity<>(endpointService.getEnabledEndpoints(), HttpStatus.OK);
    }

    @GetMapping("/endpoints/{id}")
    @Operation(summary = "Get endpoint by ID", description = "Retrieves a specific monitored endpoint by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endpoint found and retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MonitoredEndpoint.class))),
            @ApiResponse(responseCode = "404", description = "Endpoint not found")
    })
    public ResponseEntity<MonitoredEndpoint> getEndpoint(
            @Parameter(description = "ID of the endpoint to retrieve", required = true)
            @PathVariable String id) {
        Optional<MonitoredEndpoint> monitoredEndpoint = endpointService.getEndpoint(id);
        return monitoredEndpoint.map(endpoint -> new ResponseEntity<>(endpoint, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/endpoints/{id}")
    @Operation(summary = "Update endpoint", description = "Updates an existing monitored endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endpoint updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MonitoredEndpoint.class))),
            @ApiResponse(responseCode = "404", description = "Endpoint not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<MonitoredEndpoint> updateEndpoint(
            @Parameter(description = "ID of the endpoint to update", required = true)
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Endpoint update request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateMonitorRequest.class))
            )
            @RequestBody UpdateMonitorRequest request) {
        Optional<MonitoredEndpoint> updated = endpointService.updateEndpoint(id, request);
        return updated
                .map(monitoredEndpoint -> new ResponseEntity<>(monitoredEndpoint, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/endpoints/{id}")
    @Operation(summary = "Delete endpoint", description = "Deletes a monitored endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Endpoint deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Endpoint not found")
    })
    public ResponseEntity<Void> deleteEndpoint(
            @Parameter(description = "ID of the endpoint to delete", required = true)
            @PathVariable String id) {
        return endpointService.deleteEndpoint(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/endpoints/{id}/toggle")
    @Operation(summary = "Toggle endpoint status", description = "Toggles the enabled/disabled status of a monitored endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endpoint toggled successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MonitoredEndpoint.class))),
            @ApiResponse(responseCode = "404", description = "Endpoint not found")
    })
    public ResponseEntity<MonitoredEndpoint> toggleEndpoint(
            @Parameter(description = "ID of the endpoint to toggle", required = true)
            @PathVariable String id) {
        MonitoredEndpoint toggled = endpointService.toggleEndpoint(id);
        return toggled != null ? ResponseEntity.ok(toggled) : ResponseEntity.notFound().build();
    }

    @PostMapping("/endpoints/{id}/check")
    @Operation(summary = "Perform health check", description = "Performs a manual health check on a specific endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Health check completed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HealthCheckResult.class))),
            @ApiResponse(responseCode = "404", description = "Endpoint not found")
    })
    public ResponseEntity<HealthCheckResult> performHealthCheck(
            @Parameter(description = "ID of the endpoint to check", required = true)
            @PathVariable String id) {
        HealthCheckResult result = monitoringService.performSingleHealthCheck(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/status")
    @Operation(summary = "Get monitoring status", description = "Retrieves the current status of the monitoring service")
    @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
    public ResponseEntity<String> getStatus() {
        long totalEndpoints = endpointService.getAllEndpoints().size();
        long enabledEndpoints = endpointService.getEnabledEndpoints().size();
        return ResponseEntity.ok(String.format("Dynamic Monitor is running! Total endpoints: %d, Enabled: %d",
                totalEndpoints, enabledEndpoints));
    }
}