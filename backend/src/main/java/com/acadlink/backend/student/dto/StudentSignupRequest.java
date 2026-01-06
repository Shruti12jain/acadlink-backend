package com.acadlink.backend.student.dto;

public record StudentSignupRequest(
        String name,
        String email,
        String password
) {}