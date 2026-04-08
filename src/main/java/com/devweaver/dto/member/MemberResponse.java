package com.devweaver.dto.member;

import com.devweaver.entity.enums.ProjectRoles;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String email,
        String name,
        String imageUrl,
        ProjectRoles role,
        Instant invitedAt
) {
}
