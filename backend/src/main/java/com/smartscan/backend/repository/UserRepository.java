package com.smartscan.backend.repository;

import com.smartscan.backend.entity.User;
import com.smartscan.backend.entity.Role; // ✅ IMPORTANT FIX
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login)
    Optional<User> findByEmail(String email);

    // Count users by role (used for admin dashboard)
    long countByRole(Role role);
}