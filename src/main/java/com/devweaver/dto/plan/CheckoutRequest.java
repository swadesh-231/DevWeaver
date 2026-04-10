package com.devweaver.dto.plan;

import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
        @NotNull(message = "Plan ID is required")
        Long planId
) {
}
