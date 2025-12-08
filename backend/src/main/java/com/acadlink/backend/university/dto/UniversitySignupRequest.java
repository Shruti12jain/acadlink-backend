package com.acadlink.backend.university.dto;

public record UniversitySignupRequest(
        String name,
        String email,
        String password
) {}