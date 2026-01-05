package com.acadlink.backend.student.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.acadlink.backend.student.dto.StudentSignupRequest;
import com.acadlink.backend.student.dto.StudentSignupResponse;

import  com.acadlink.backend.student.repository.StudentRepository;
import com.acadlink.backend.university.repository.UniversityRepository;
import com.acadlink.backend.utils.JwtUtils;
import java.util.UUID;
import com.acadlink.backend.student.entity.Student;
import com.acadlink.backend.university.entity.University;

@Service
public class StudentAuthService {
    private final UniversityRepository UniversityRepository;
    private final JwtUtils jwtUtils;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentAuthService(JwtUtils jwtUtils, StudentRepository studentRepository, PasswordEncoder passwordEncoder, UniversityRepository universityRepository) {
        this.jwtUtils = jwtUtils;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.UniversityRepository = universityRepository;
    }

    public StudentSignupResponse signup(StudentSignupRequest request, UUID universityId) {
    // check if university exists because student can only be registered by university and professor 
    if (!UniversityRepository.existsById(universityId)) {
        throw new IllegalArgumentException("University does not exist");
    }
    // check if student already exists 
    if (studentRepository.existsByEmail(request.email())) {
        throw new IllegalArgumentException("Email already registered");
    }
     // create new student
    Student student = new Student();
    student.setName(request.name());
    student.setPassword(passwordEncoder.encode(request.password()));
    student.setEmail(request.email());
    // save student
    Student saved = studentRepository.save(student);
    // associate student with university
    University university = UniversityRepository.findById(universityId)
            .orElseThrow(() -> new IllegalArgumentException("University not found"));

    university.getStudents().add(saved);
    UniversityRepository.save(university);

    return new StudentSignupResponse(
            saved.getId().toString(),
            saved.getEmail()
    );
  }
}