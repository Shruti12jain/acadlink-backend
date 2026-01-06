package com.acadlink.backend.professor.service;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.acadlink.backend.professor.dto.ProfessorSignupResponse;
import com.acadlink.backend.professor.repository.ProfessorRepository;
import com.acadlink.backend.university.entity.University;
import com.acadlink.backend.university.repository.UniversityRepository;
import com.acadlink.backend.utils.JwtUtils;
import com.acadlink.backend.professor.dto.ProfessorSignupRequest;
import java.util.UUID;
import com.acadlink.backend.professor.entity.Professor;


@Service
public class ProfessorAuthService {
    private final UniversityRepository universityRepository;
    private final JwtUtils jwtUtils;
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfessorAuthService(JwtUtils jwtUtils, ProfessorRepository professorRepository, PasswordEncoder passwordEncoder, UniversityRepository universityRepository) {
        this.jwtUtils = jwtUtils;
        this.professorRepository = professorRepository;
        this.passwordEncoder = passwordEncoder;
        this.universityRepository = universityRepository;
    }
    
    public ProfessorSignupResponse signup (ProfessorSignupRequest request,UUID universityId ){
    //check if university exists because professor can only register when university exist 
    if(!universityRepository.existsById(universityId)){
            throw new IllegalArgumentException("University does not exist");
    }
    //check if professor already exists
    if(professorRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("Email already registered");
    }
    // associate student with university
    University university = universityRepository.findById(universityId)
            .orElseThrow(() -> new IllegalArgumentException("University not found"));

    //create new professor 
    Professor professor = new Professor();
    professor.setName(request.name());
    professor.setEmail(request.email());
    professor.setPassword(passwordEncoder.encode(request.password()));
    professor.setUniversity(university);
    //save professor 
    Professor saved = professorRepository.save(professor);
    
    return new ProfessorSignupResponse(
        saved.getId().toString(),
        saved.getEmail()
    );

    
  }

}    