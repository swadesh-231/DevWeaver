package com.devweaver.entity;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectFile {
    private Long id;
    private Project project;
    private String path;
    private String minIoObjectKey;
    private User created_by;
    private User updated_by;

    private Instant created_at;
    private Instant deleted_at;
}
