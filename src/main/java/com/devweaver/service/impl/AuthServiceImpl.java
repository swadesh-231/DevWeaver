package com.devweaver.service.impl;

import com.devweaver.dto.auth.AuthResponse;
import com.devweaver.dto.auth.LoginRequest;
import com.devweaver.dto.auth.RegisterRequest;
import com.devweaver.entity.User;
import com.devweaver.exception.DuplicateResourceException;
import com.devweaver.mapper.UserMapper;
import com.devweaver.repository.UserRepository;
import com.devweaver.security.jwt.JwtUtils;
import com.devweaver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse registerUser(RegisterRequest registerRequest) {
        userRepository.findByEmail(registerRequest.email())
                .ifPresent(user -> {
                    throw new DuplicateResourceException("User", "email", registerRequest.email());
                });
        User user = User.builder()
                .username(registerRequest.name())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .build();
        userRepository.save(user);
        String token = jwtUtils.generateAccessToken(user);
        return new AuthResponse(token, userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );
        User user = (User) authentication.getPrincipal();
        String token = jwtUtils.generateAccessToken(user);
        return new AuthResponse(token, userMapper.toUserProfileResponse(user));
    }
}
