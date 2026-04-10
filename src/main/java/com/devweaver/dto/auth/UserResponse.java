package com.devweaver.dto.auth;

public record UserResponse(
        Long id,
        String email,
        String username,
        String imageUrl
) {
}
