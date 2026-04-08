package com.devweaver.dto.auth;

public record LoginRequest(
        String email, String password
) {
}
