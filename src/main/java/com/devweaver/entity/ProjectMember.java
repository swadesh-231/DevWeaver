package com.devweaver.entity;

import com.devweaver.entity.enums.ProjectRoles;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "projects_members")
public class ProjectMember {
    @EmbeddedId
    private ProjectMemberId projectMemberId;
    @ManyToOne
    @MapsId("projectId")
    private Project project;
    @ManyToOne
    @MapsId("userId")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectRoles projectRole;
    private Instant invited_at;
    private Instant accepted_at;

}
