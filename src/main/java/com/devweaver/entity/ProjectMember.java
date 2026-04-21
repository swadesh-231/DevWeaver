package com.devweaver.entity;

import com.devweaver.entity.enums.ProjectRole;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectRole projectRole;
    private Instant invited_at;
    private Instant accepted_at;

}
