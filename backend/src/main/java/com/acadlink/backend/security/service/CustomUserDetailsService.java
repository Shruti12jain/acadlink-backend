package com.acadlink.backend.security.service;


import com.acadlink.backend.professor.repository.ProfessorRepository;
import com.acadlink.backend.security.model.Role;
import com.acadlink.backend.security.model.UserPrincipal;
import com.acadlink.backend.student.repository.StudentRepository;
import com.acadlink.backend.university.repository.UniversityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final UniversityRepository universityRepository;

    public CustomUserDetailsService(
            StudentRepository studentRepository,
            ProfessorRepository professorRepository,
            UniversityRepository universityRepository
    ) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.universityRepository = universityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1️ Check Student
        return studentRepository.findByEmail(email)
            .map(student -> new UserPrincipal(
                    student.getId().toString(),
                    student.getEmail(),
                    student.getPassword(),
                    Role.STUDENT
            )).orElseGet(() ->

        // 2️ Check Professor
        professorRepository.findByEmail(email)
            .map(professor -> new UserPrincipal(
                    professor.getId().toString(),
                    professor.getEmail(),
                    professor.getPassword(),
                    Role.PROFESSOR
            )).orElseGet(() ->

        // 3️ Check University Admin
        universityRepository.findByEmail(email)
            .map(university -> new UserPrincipal(
                    university.getId().toString(),
                    university.getEmail(),
                    university.getPassword(),
                    Role.UNIVERSITY
            )).orElseThrow(() ->
                    // 4️ Nothing found → throw error
                    new UsernameNotFoundException("No user found with email: " + email)
            )));
    }
}
