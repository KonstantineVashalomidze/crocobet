package com.crocobet.konstantinevashalomidze.repository;

import com.crocobet.konstantinevashalomidze.model.MonitoredEndpoint;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EndpointRepository {
    private final Map<String, MonitoredEndpoint> endpoints = new ConcurrentHashMap<>();

    public MonitoredEndpoint save(MonitoredEndpoint endpoint) {
        endpoints.put(endpoint.getId(), endpoint);
        return endpoint;
    }

    public Optional<MonitoredEndpoint> findById(String id) {
        return Optional.ofNullable(endpoints.get(id));
    }

    public List<MonitoredEndpoint> findAll() {
        return new ArrayList<>(endpoints.values());
    }

    public List<MonitoredEndpoint> findAllEnabled() {
        return endpoints.values().stream()
                .filter(MonitoredEndpoint::isEnabled)
                .toList();
    }


    public void deleteById(String id) {
        endpoints.remove(id);
    }


    public boolean existsById(String id) {
        return endpoints.containsKey(id);
    }

    public long count() {
        return endpoints.size();
    }




}
