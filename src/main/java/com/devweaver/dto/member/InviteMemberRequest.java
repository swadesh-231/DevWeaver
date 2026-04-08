package com.devweaver.dto.member;

import com.devweaver.entity.enums.ProjectRoles;

public record InviteMemberRequest(
        String email,
        ProjectRoles role
) {
}
