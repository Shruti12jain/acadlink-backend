package com.acadlink.backend.university.repository;

import com.acadlink.backend.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UniversityRepository extends JpaRepository<University, UUID> {

    Optional<University> findByEmail(String email);
    boolean existsByName(String name);
    boolean existsByEmail(String Email);
}
