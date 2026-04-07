package com.devweaver.entity;

import com.devweaver.entity.enums.PreviewStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Preview {
    private Long id;
    private Project project;
    private String name_space;
    private String pod_name;
    private String preview_url;

    @Enumerated(EnumType.STRING)
    private PreviewStatus status;

    private Instant startedAt;
    private Instant terminatedAt;

    private Instant createdAt;
}
