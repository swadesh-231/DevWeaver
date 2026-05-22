package com.devweaver.service;


import com.devweaver.dto.plan.SubscriptionResponse;
import com.devweaver.entity.enums.SubscriptionStatus;

import java.time.Instant;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription();
    void activateSubscription(Long useId, Long planId, String subscriptionId, String customerId);
    void updateSubscription(String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);
    void cancelSubscription(String subscriptionId);
    void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd);
    void markSubscriptionPastDue(String subId);
    boolean canCreateNewProject();
}
