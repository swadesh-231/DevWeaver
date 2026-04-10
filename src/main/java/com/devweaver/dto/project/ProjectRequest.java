package com.devweaver.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectRequest(
        @NotBlank(message = "Project name is required")
        @Size(min = 3, max = 100, message = "Project name must be between 3 and 100 characters")
        String name
) {
}

