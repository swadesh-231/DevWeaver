package com.devweaver.controller;

import com.devweaver.dto.auth.AuthResponse;
import com.devweaver.dto.auth.LoginRequest;
import com.devweaver.dto.auth.RegisterRequest;
import com.devweaver.dto.auth.UserResponse;
import com.devweaver.security.jwt.JwtUtils;
import com.devweaver.service.AuthService;
import com.devweaver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.registerUser(registerRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    @GetMapping("/user")
    public ResponseEntity<UserResponse> getProfile(){
        Long userId = jwtUtils.getCurrentUserId();
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
