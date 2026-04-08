package com.devweaver.service;

import com.devweaver.dto.auth.AuthResponse;
import com.devweaver.dto.auth.LoginRequest;
import com.devweaver.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse registerUser(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
