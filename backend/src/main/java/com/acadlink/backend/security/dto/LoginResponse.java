package com.acadlink.backend.security.dto;

public record LoginResponse(String token, String role, String userId) { }