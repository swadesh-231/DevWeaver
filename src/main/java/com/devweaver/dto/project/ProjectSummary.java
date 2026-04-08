package com.devweaver.dto.project;

import java.time.Instant;

public record ProjectSummary(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
