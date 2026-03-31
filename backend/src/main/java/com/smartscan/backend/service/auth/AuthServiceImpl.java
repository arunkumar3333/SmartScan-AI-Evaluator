package com.smartscan.backend.service.auth;

import com.smartscan.backend.dto.auth.AuthResponseDto;
import com.smartscan.backend.dto.auth.LoginRequestDto;
import com.smartscan.backend.dto.auth.RegisterRequestDto;
import com.smartscan.backend.entity.Role;
import com.smartscan.backend.entity.User;
import com.smartscan.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid role. Use ADMIN or TEACHER");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .approved(role == Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        return AuthResponseDto.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .token("dummy-token")
                .message(
                        savedUser.getRole() == Role.TEACHER
                                ? "Registration successful. Wait for admin approval."
                                : "Admin registered successfully"
                )
                .build();
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (user.getRole() == Role.TEACHER && !user.isApproved()) {
            throw new RuntimeException("Admin approval is pending");
        }

        return AuthResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token("dummy-token")
                .message("Login successful")
                .build();
    }
}