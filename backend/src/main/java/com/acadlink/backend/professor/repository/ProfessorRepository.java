package com.acadlink.backend.professor.repository;

import com.acadlink.backend.professor.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {

    Optional<Professor> findByEmail(String email);

    boolean existsByEmail(String email);
}
