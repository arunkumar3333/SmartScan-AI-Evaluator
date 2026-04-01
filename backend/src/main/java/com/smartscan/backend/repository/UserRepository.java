package com.smartscan.backend.repository;

import com.smartscan.backend.entity.Role;
import com.smartscan.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email for login
    Optional<User> findByEmail(String email);

    // Count teachers for admin dashboard
    long countByRole(Role role);

    // Get all teachers
    List<User> findByRole(Role role);

    // Get teachers by approval status
    List<User> findByRoleAndApproved(Role role, boolean approved);
}