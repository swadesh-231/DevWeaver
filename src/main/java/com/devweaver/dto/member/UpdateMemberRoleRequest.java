package com.devweaver.dto.member;

import com.devweaver.entity.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
        @NotNull(message = "Role is required")
        ProjectRole role
) {
}
