package com.devweaver.dto.member;

import com.devweaver.entity.enums.ProjectRoles;

public record UpdateMemberRoleRequest(ProjectRoles role) {
}
