package com.devweaver.entity;

import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    private Long id;
    private String name;
    private User creator;
    private Boolean isPublic = false;

    private Instant createdAt;
    private Instant updatedAt;

    private Instant deletedAt;
}
