package com.smartscan.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User name
    private String name;

    // Unique email
    @Column(unique = true, nullable = false)
    private String email;

    // Encoded password
    @Column(nullable = false)
    private String password;

    // Role: ADMIN or TEACHER
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Admin approval status
    // ADMIN -> true by default
    // TEACHER -> false by default
    private boolean approved;

    // Created time
    private LocalDateTime createdAt;
}