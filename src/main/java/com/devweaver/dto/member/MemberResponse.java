package com.devweaver.dto.member;

import com.devweaver.entity.enums.ProjectRoles;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String email,
        String name,
        ProjectRoles projectRoles,
        Instant invitedAt
) {
}
