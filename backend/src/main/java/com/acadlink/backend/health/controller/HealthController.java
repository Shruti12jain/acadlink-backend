package com.acadlink.backend.health.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acadlink.backend.health.model.HealthResponse;
import com.acadlink.backend.health.service.HealthService;

@RestController
@RequestMapping("/health")
public class HealthController {
    
     private final HealthService healthService;

      public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

     // Basic app health
    @GetMapping
    public ResponseEntity<HealthResponse> basicHealth() {
        return ResponseEntity.ok(healthService.checkAppHealth());
    }

    // Database health
    @GetMapping("/db")
    public ResponseEntity<HealthResponse> dbHealth() {
        return ResponseEntity.ok(healthService.checkDatabaseHealth());
    }

    // External dependencies (email, storage, etc.)
    @GetMapping("/dependencies")
    public ResponseEntity<HealthResponse> dependenciesHealth() {
        return ResponseEntity.ok(healthService.checkDependenciesHealth());
    }

    // Readiness probe (is app ready to receive traffic?)
    @GetMapping("/ready")
    public ResponseEntity<HealthResponse> readiness() {
        return ResponseEntity.ok(healthService.checkReadiness());
    }

    // Liveness probe (is app process alive?)
    @GetMapping("/live")
    public ResponseEntity<Map<String, String>> liveness() {
        return ResponseEntity.ok(Map.of("status", "ALIVE"));
    }
}
