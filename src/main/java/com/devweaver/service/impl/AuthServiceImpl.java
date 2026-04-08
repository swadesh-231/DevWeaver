package com.devweaver.service.impl;

import com.devweaver.dto.auth.AuthResponse;
import com.devweaver.dto.auth.LoginRequest;
import com.devweaver.dto.auth.RegisterRequest;
import com.devweaver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse registerUser(RegisterRequest registerRequest) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        return null;
    }
}
