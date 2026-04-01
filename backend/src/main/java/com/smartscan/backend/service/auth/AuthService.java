package com.smartscan.backend.service.auth;

import com.smartscan.backend.dto.auth.AuthResponseDto;
import com.smartscan.backend.dto.auth.LoginRequestDto;
import com.smartscan.backend.dto.auth.RegisterRequestDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(LoginRequestDto request);
}