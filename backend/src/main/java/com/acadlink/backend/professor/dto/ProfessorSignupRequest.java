package com.acadlink.backend.professor.dto;

public record ProfessorSignupRequest(
        String name,
        String email,
        String password
) {}