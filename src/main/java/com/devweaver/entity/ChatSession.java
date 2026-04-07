package com.devweaver.entity;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatSession {
    private Project project;
    private User user;
    private String title;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

}
