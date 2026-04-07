package com.devweaver.entity;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLog {
    private Long id;
    private User user;
    private Project project;

    private String action;

    private Integer tokensUsed;
    private Integer durationMs;

    private String metaData; // JSON of {model_used, prompt_used},

    private Instant createdAt;
}
