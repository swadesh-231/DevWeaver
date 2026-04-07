package com.devweaver.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan {
    private Long id;
    private String name;
    private String stripe_price_id;
    private Integer max_projects;
    private Integer max_token_per_day;
    private Integer max_preview;
    private Boolean unlimited_ai;

    private Boolean isActive;


}
