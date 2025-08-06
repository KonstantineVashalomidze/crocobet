package com.crocobet.konstantinevashalomidze.service;

import com.crocobet.konstantinevashalomidze.model.HealthCheckResult;
import com.crocobet.konstantinevashalomidze.model.MonitoredEndpoint;
import com.crocobet.konstantinevashalomidze.repository.EndpointRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DynamicMonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(DynamicMonitoringService.class);

    private final EndpointRepository endpointRepository;
    private final DynamicHealthCheckService healthCheckService;
    private final MeterRegistry meterRegistry;

    public DynamicMonitoringService(EndpointRepository endpointRepository, DynamicHealthCheckService healthCheckService, MeterRegistry meterRegistry) {
        this.endpointRepository = endpointRepository;
        this.healthCheckService = healthCheckService;
        this.meterRegistry = meterRegistry;
    }


    private final Map<String, Counter> successCounters = new ConcurrentHashMap<>();
    private final Map<String, Counter> failureCounters = new ConcurrentHashMap<>();
    private final Map<String, Timer> responseTimeTimers = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> lastResponseTimes = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> lastStatusCodes = new ConcurrentHashMap<>();


    @PostConstruct
    public void initializeMetrics() {
        Gauge.builder("monitored_endpoints_total", this, service -> service.endpointRepository.count())
                .description("Total number of monitored endpoints")
                .register(meterRegistry);
    }


    @Scheduled(fixedRate = 30 * 1000)
    public void performScheduledHealthChecks() {
        List<MonitoredEndpoint> enabledEndpoints = endpointRepository.findAllEnabled();

        if (enabledEndpoints.isEmpty()) {
            logger.debug("No enabled endpoints to monitor");
            return;
        }

        logger.info("Starting scheduled health checks for {} endpoints", enabledEndpoints.size());

        for (MonitoredEndpoint endpoint : enabledEndpoints) {
            try {
                HealthCheckResult result = healthCheckService.checkEndpoint(endpoint);
                updateMetrics(endpoint, result);
            } catch (Exception e) {
                logger.error("Error during health check for endpoint {}: {}", endpoint.getName(), e.getMessage());
            }
        }
    }

    public HealthCheckResult performSingleHealthCheck(String endpointId) {
        return endpointRepository.findById(endpointId)
                .map(endpoint -> {
                    HealthCheckResult result = healthCheckService.checkEndpoint(endpoint);
                    updateMetrics(endpoint, result);
                    return result;
                })
                .orElse(null);
    }


    private void updateMetrics(MonitoredEndpoint endpoint, HealthCheckResult result) {
        String endpointId = endpoint.getId();
        String endpointName = endpoint.getName();

        // Create metrics if they don't exist
        ensureMetricsExist(endpointId, endpointName);

        // Update counters
        if (result.isHealthy()) {
            successCounters.get(endpointId).increment();
        } else {
            failureCounters.get(endpointId).increment();
        }

        // Update timers and gauges
        responseTimeTimers.get(endpointId).record(result.getResponseTimeMs(), TimeUnit.MILLISECONDS);
        lastResponseTimes.get(endpointId).set(result.getResponseTimeMs());
        lastStatusCodes.get(endpointId).set(result.getStatusCode());
    }


    private void ensureMetricsExist(String endpointId, String endpointName) {
        successCounters.computeIfAbsent(endpointId, id ->
                Counter.builder("endpoint_health_check_success_total")
                        .description("Total number of successful health checks")
                        .tag("endpoint_id", id)
                        .tag("endpoint_name", endpointName)
                        .register(meterRegistry));

        failureCounters.computeIfAbsent(endpointId, id ->
                Counter.builder("endpoint_health_check_failure_total")
                        .description("Total number of failed health checks")
                        .tag("endpoint_id", id)
                        .tag("endpoint_name", endpointName)
                        .register(meterRegistry));

        responseTimeTimers.computeIfAbsent(endpointId, id ->
                Timer.builder("endpoint_response_time")
                        .description("Response time for health checks")
                        .tag("endpoint_id", id)
                        .tag("endpoint_name", endpointName)
                        .register(meterRegistry));

        lastResponseTimes.computeIfAbsent(endpointId, id -> {
            AtomicLong responseTime = new AtomicLong(0);
            Gauge.builder("endpoint_last_response_time_ms", responseTime, AtomicLong::get)
                    .description("Last response time in milliseconds")
                    .tag("endpoint_id", id)
                    .tag("endpoint_name", endpointName)
                    .register(meterRegistry);
            return responseTime;
        });

        lastStatusCodes.computeIfAbsent(endpointId, id -> {
            AtomicLong statusCode = new AtomicLong(0);
            Gauge.builder("endpoint_last_status_code", statusCode, AtomicLong::get)
                    .description("Last HTTP status code received")
                    .tag("endpoint_id", id)
                    .tag("endpoint_name", endpointName)
                    .register(meterRegistry);
            return statusCode;
        });
    }

    public void removeEndpointMetrics(String endpointId) {
        successCounters.remove(endpointId);
        failureCounters.remove(endpointId);
        responseTimeTimers.remove(endpointId);
        lastResponseTimes.remove(endpointId);
        lastStatusCodes.remove(endpointId);
    }

}
