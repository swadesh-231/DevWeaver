package com.devweaver.entity;

import com.devweaver.entity.enums.SubscriptionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {
    private Long id;
    private User user;
    private Plan plan;

    private String stripe_subscription_id;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;
    @Builder.Default
    private Boolean cancelled_at_period_end = false;


    private Instant created_at;
    private Instant updated_at;
}
