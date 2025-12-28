package com.acadlink.backend.university.dto;

public record UniversitySignupResponse(
        String id,
        String email,
        String token
) {}
