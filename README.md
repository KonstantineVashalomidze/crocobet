# Crocobet Monitoring System

## Table of Contents
1. [Introduction](#introduction)
2. [System Overview](#system-overview)
3. [Architecture](#architecture)
4. [Prerequisites](#prerequisites)
5. [Installation and Setup](#installation-and-setup)
6. [Usage Flow](#usage-flow)
7. [Docker Commands](#docker-commands)
8. [Troubleshooting](#troubleshooting)

---

## Introduction

The [__crocobet__](https://github.com/KonstantineVashalomidze/crocobet) is a http server health monitoring too built with Spring Boot that monitors HTTP/HTTPS endpoints and provides detailed metrics for observability. The system is also a Prometheus exporter, which collects health check data and exposing it for monitoring and visualization through Grafana dashboards.

## System Overview

This monitoring system provides dynamic endpoint monitoring with configurable health checks, HTTP/HTTPS request validation including body and header verification, response time tracking and performance metrics, Prometheus integration for metrics collection, Grafana dashboards for visualization, REST API for endpoint management, Swagger documentation for easy testing, and Docker containerization for easy deployment.

## Architecture

The system consists of three containerized applications running Spring Boot Application on port 8080, Prometheus for metrics collection on port 9090, and Grafana for dashboards on port 3000.

The data flow works like this: Spring Boot performs health checks on monitored endpoints, Micrometer formats metrics for Prometheus, Prometheus scrapes metrics every 15 seconds from the actuator prometheus endpoint, and Grafana queries Prometheus and displays dashboards.

## Prerequisites

Docker and Docker Compose must be installed, or Docker Desktop which contains both. You need at least 2GB of available RAM and ports 3000, 8080, and 9090 available on your system.

## Installation and Setup

Clone the repository. Build and run the containers using `docker-compose up --build`. Verify the services are running by accessing Spring Boot Application at [Spring Boot](http://localhost:8080), Prometheus at [Prometheus](http://localhost:9090), and Grafana at [Grafana](http://localhost:3000). Access Grafana using username `admin` and password `admin`. 

## Usage Flow

At application startup, the system automatically begins monitoring 22 default endpoints from crocobet.com. The system performs automated health checks every 20 seconds for all enabled endpoints.

To add new endpoints for monitoring, access the Swagger UI at [Swagger](http://localhost:8080/swagger-ui/index.html#/). Use the REST API to create new monitoring targets by sending a any type of http request to the endpoints API with your target URL, HTTP method, expected status codes, and validation rules. The system will automatically begin monitoring the new endpoint and expose metrics to Prometheus.

Monitor your endpoints through Grafana dashboards at [Grafana](http://localhost:3000). The dashboards display health check success and failure rates, response times and performance trends, HTTP status codes, and overall system health metrics. You can modify existing endpoint configurations or trigger manual health checks through the API as needed.

For testing and validation, use the Swagger interface to test all API endpoints directly, add new monitoring targets, modify existing configurations, trigger manual health checks, and view detailed endpoint information.

## Docker Commands

To build and start all services use `docker-compose up --build`. Start services in detached mode with `docker-compose up -d`. Stop all services using `docker-compose down`. Restart a specific service with `docker-compose restart crocobet`. 

## Troubleshooting

Common issues include port conflicts where you should ensure ports 3000, 8080, and 9090 are available and use `docker-compose down` to stop services if needed. For container startup issues, check container logs using `docker logs crocobet`, `docker logs prometheus`, or `docker logs grafana`.

If metrics are not appearing, verify Prometheus is scraping by checking http://localhost:9090/targets and confirm the Spring Boot actuator endpoint is accessible at http://localhost:8080/actuator/prometheus. For Grafana dashboard issues, verify the data source connection in Grafana settings and check if dashboards are properly provisioned.

Debugging can be accomplished by viewing all containers with `docker-compose ps`, restarting specific services using `docker-compose restart crocobet`, rebuilding without cache using `docker-compose build --no-cache`, and monitoring container resource usage with `docker stats`.

Access application logs through `docker logs crocobet`, check the metrics endpoint at http://localhost:8080/actuator/prometheus, verify the health check endpoint at http://localhost:8080/actuator/health, and monitor Prometheus targets at http://localhost:9090/targets.

