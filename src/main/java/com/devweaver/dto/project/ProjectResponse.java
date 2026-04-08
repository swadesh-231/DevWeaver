package com.devweaver.dto.project;

import com.devweaver.dto.auth.UserResponse;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        UserResponse owner
) {
}
