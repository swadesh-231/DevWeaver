package com.devweaver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "users")
public class User {
    private Long id;
    private String username;

    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,20}$",
            message = "Password must be 8-20 chars, include upper, lower, number, and special char"
    )
    private String password;
    @Column(unique = true)
    private String email;
    private String image_url;

    private Instant created_at;
    private Instant updated_at;
    private Instant deleted_at;
}
