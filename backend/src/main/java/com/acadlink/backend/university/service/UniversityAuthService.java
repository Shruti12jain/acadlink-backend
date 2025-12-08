package com.acadlink.backend.university.service;

import com.acadlink.backend.university.dto.UniversitySignupRequest;
import com.acadlink.backend.university.dto.UniversitySignupResponse;
import com.acadlink.backend.university.entity.University;
import com.acadlink.backend.university.repository.UniversityRepository;
import com.acadlink.backend.security.model.Role;
import com.acadlink.backend.utils.JwtUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UniversityAuthService {

    private final UniversityRepository universityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UniversityAuthService(
            UniversityRepository universityRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils
    ) {
        this.universityRepository = universityRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public UniversitySignupResponse signup(UniversitySignupRequest request) {

        if (universityRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        University university = new University();
        university.setName(request.name());
        university.setEmail(request.email());
        university.setPassword(passwordEncoder.encode(request.password()));

        University saved = universityRepository.save(university);

        // Prepare JWT token
        String token = jwtUtils.generateToken(
                saved.getId().toString(),
                saved.getEmail(),
                Role.UNIVERSITY
        );

        return new UniversitySignupResponse(
                saved.getId().toString(),
                saved.getEmail(),
                token
        );
    }
}
