package com.acadlink.backend.health.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.acadlink.backend.health.model.HealthResponse;
import com.acadlink.backend.health.model.HealthStatus;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthService {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.application.name:AcadLink Backend}")
    private String serviceName;

    @Value("${spring.application.version:1.0.0}")
    private String serviceVersion;

    public HealthService(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    // ---  Basic App Health ---
    public HealthResponse checkAppHealth() {
        return HealthResponse.builder()
                .status(HealthStatus.UP)
                .service(serviceName)
                .version(serviceVersion)
                .timestamp(Instant.now().toString())
                .details(Map.of("description", "Application is running fine"))
                .build();
    }

    // --- Database Health ---
    public HealthResponse checkDatabaseHealth() {
        Map<String, Object> details = new HashMap<>();
        long start = System.currentTimeMillis();

        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            details.put("database", "PostgreSQL");
            details.put("connection", "Active");
            details.put("latencyMs", System.currentTimeMillis() - start);

            return HealthResponse.builder()
                    .status(HealthStatus.UP)
                    .service(serviceName)
                    .version(serviceVersion)
                    .timestamp(Instant.now().toString())
                    .details(details)
                    .build();
        } catch (Exception e) {
            details.put("error", e.getMessage());
            return HealthResponse.builder()
                    .status(HealthStatus.DOWN)
                    .service(serviceName)
                    .version(serviceVersion)
                    .timestamp(Instant.now().toString())
                    .details(details)
                    .build();
        }
    }

    // --- Dependencies Health ---
    public HealthResponse checkDependenciesHealth() {
        // In future you can check email, S3, external APIs etc.
        Map<String, Object> dependencies = new HashMap<>();
        dependencies.put("emailService", "UP");
        dependencies.put("storage", "UP");

        return HealthResponse.builder()
                .status(HealthStatus.UP)
                .service(serviceName)
                .version(serviceVersion)
                .timestamp(Instant.now().toString())
                .details(dependencies)
                .build();
    }

    // ---  Readiness Probe ---
    public HealthResponse checkReadiness() {
        HealthResponse dbStatus = checkDatabaseHealth();

        Map<String, Object> details = new HashMap<>();
        details.put("app", "UP");
        details.put("database", dbStatus.getStatus().toString());
        details.put("flywayMigrations", "COMPLETED");

        HealthStatus overallStatus = dbStatus.getStatus() == HealthStatus.UP
                ? HealthStatus.READY : HealthStatus.DOWN;

        return HealthResponse.builder()
                .status(overallStatus)
                .service(serviceName)
                .version(serviceVersion)
                .timestamp(Instant.now().toString())
                .details(details)
                .build();
    }
}
