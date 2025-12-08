package com.acadlink.backend.university.entity;

import com.acadlink.backend.professor.entity.Professor;
import com.acadlink.backend.security.model.Role;
import com.acadlink.backend.student.entity.Student;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "universities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class University {

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
    private Role role = Role.UNIVERSITY;

        // ONE-TO-MANY: University → Professors
    @OneToMany(mappedBy = "university")
    private Set<Professor> professors = new HashSet<>();

  
    // MANY-TO-MANY: University ↔ Students
    @ManyToMany
    @JoinTable(
            name = "university_students",
            joinColumns = @JoinColumn(name = "university_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
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
