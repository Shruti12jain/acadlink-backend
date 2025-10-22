package com.acadlink.backend.health.model;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class HealthResponse {
    private HealthStatus status;
    private String service;
    private String version;
    private String timestamp;
    private Map<String, Object> details;
}

