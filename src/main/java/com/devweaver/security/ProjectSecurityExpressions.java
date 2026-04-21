package com.devweaver.security;

import com.devweaver.entity.enums.ProjectPermission;
import com.devweaver.repository.ProjectMemberRepository;
import com.devweaver.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("security")
@RequiredArgsConstructor
public class ProjectSecurityExpressions {
    private final ProjectMemberRepository projectMemberRepository;
    private final JwtUtils jwtUtils;

    public boolean hasPermission(Long projectId, ProjectPermission projectPermission) {
        Long userId = jwtUtils.getCurrentUserId();

        return projectMemberRepository.findRoleByProjectIdAndUserId(projectId, userId).
                map(role -> role.getPermissions().contains(projectPermission))
                .orElse(false);
    }
    public boolean canViewProject(Long projectId) {
        return hasPermission(projectId, ProjectPermission.VIEW);
    }

    public boolean canEditProject(Long projectId) {
        return hasPermission(projectId, ProjectPermission.EDIT);
    }

    public boolean canDeleteProject(Long projectId) {
        return hasPermission(projectId, ProjectPermission.DELETE);
    }

    public boolean canViewMembers(Long projectId) {
        return hasPermission(projectId, ProjectPermission.VIEW_MEMBERS);
    }

    public boolean canManageMembers(Long projectId) {
        return hasPermission(projectId, ProjectPermission.MANAGE_MEMBERS);
    }

}
