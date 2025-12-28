package com.acadlink.backend.professor.entity;

import com.acadlink.backend.security.model.Role;
import com.acadlink.backend.student.entity.Student;
import com.acadlink.backend.university.entity.University;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "professors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professor {

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
    private Role role = Role.PROFESSOR;


    // MANY-TO-ONE: Professor → University
    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;


    // MANY-TO-MANY: Professor ↔ Students
    @ManyToMany
    @JoinTable(
            name = "professor_students",
            joinColumns = @JoinColumn(name = "professor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id")
    )
    private Set<Student> students = new HashSet<>();
    
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
