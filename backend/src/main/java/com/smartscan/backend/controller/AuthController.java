package com.smartscan.backend.controller;

import com.smartscan.backend.dto.auth.AuthResponseDto;
import com.smartscan.backend.dto.auth.LoginRequestDto;
import com.smartscan.backend.dto.auth.RegisterRequestDto;
import com.smartscan.backend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/admin-only")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("Only ADMIN can access this");
    }

    @GetMapping("/teacher-only")
    public ResponseEntity<String> teacherOnly() {
        return ResponseEntity.ok("Only TEACHER can access this");
    }
}