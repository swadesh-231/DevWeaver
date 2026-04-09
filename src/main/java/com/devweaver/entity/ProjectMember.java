package com.devweaver.entity;

import com.devweaver.entity.enums.ProjectRoles;

import java.time.Instant;

public class ProjectMember {
    private ProjectMemberId projectMemberId;
    private Project project;
    private User user;
    private ProjectRoles projectRole;
    private Instant invited_at;
    private Instant accepted_at;

}
