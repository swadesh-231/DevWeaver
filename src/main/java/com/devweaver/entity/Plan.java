package com.devweaver.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String stripe_price_id;
    private Integer max_projects;
    private Integer max_token_per_day;
    private Integer max_preview;
    private Boolean unlimited_ai;

    private Boolean isActive;


}
