package com.smartscan.backend.dto.auth;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private String role; // ADMIN or TEACHER
}