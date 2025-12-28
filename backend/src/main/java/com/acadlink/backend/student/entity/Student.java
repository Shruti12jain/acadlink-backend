package com.acadlink.backend.student.entity;

import com.acadlink.backend.professor.entity.Professor;
import com.acadlink.backend.security.model.Role;
import com.acadlink.backend.university.entity.University;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // hashed password

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.STUDENT;

    // MANY-TO-MANY: Student ↔ University
    @ManyToMany(mappedBy = "students")
    private Set<University> universities = new HashSet<>();


    // MANY-TO-MANY: Student ↔ Professor
    @ManyToMany(mappedBy = "students")
    private Set<Professor> professors = new HashSet<>();


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
