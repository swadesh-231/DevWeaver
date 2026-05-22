package com.devweaver.service.impl;

import com.devweaver.dto.plan.SubscriptionResponse;
import com.devweaver.entity.Plan;
import com.devweaver.entity.Subscription;
import com.devweaver.entity.User;
import com.devweaver.entity.enums.SubscriptionStatus;
import com.devweaver.exception.ResourceNotFoundException;
import com.devweaver.mapper.SubscriptionMapper;
import com.devweaver.repository.PlanRepository;
import com.devweaver.repository.ProjectMemberRepository;
import com.devweaver.repository.SubscriptionRepository;
import com.devweaver.repository.UserRepository;
import com.devweaver.security.jwt.JwtUtils;
import com.devweaver.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private final JwtUtils jwtUtils;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId = jwtUtils.getCurrentUserId();
        var currentSubscription = subscriptionRepository.findByUserIdAndStatusIn(
                userId, Set.of(SubscriptionStatus.ACTIVE,SubscriptionStatus.PAST_DUE,SubscriptionStatus.TRAILING
        ))
                .orElse(new Subscription());

        return subscriptionMapper.toSubscriptionResponse(currentSubscription);

    }

    @Override
    public void activateSubscription(Long useId, Long planId, String subscriptionId, String customerId) {
        boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if (exists) return;
        User user = userRepository.findById(useId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", useId));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", planId));

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();

        subscriptionRepository.save(subscription);

    }

    @Override
    public void updateSubscription(String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {
        Subscription subscription = subscriptionRepository.findByStripeSubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", subscriptionId));
        boolean hasSubscriptionUpdated = false;
        if(status != null && status != subscription.getStatus()) {
            subscription.setStatus(status);
            hasSubscriptionUpdated = true;
        }
        if(periodStart != null && !periodStart.equals(subscription.getCurrentPeriodStart())) {
            subscription.setCurrentPeriodStart(periodStart);
            hasSubscriptionUpdated = true;
        }

        if(periodEnd != null && !periodEnd.equals(subscription.getCurrentPeriodEnd())) {
            subscription.setCurrentPeriodEnd(periodEnd);
            hasSubscriptionUpdated = true;
        }

        if(cancelAtPeriodEnd != null && cancelAtPeriodEnd != subscription.getCancelled_at_period_end()) {
            subscription.setCancelled_at_period_end(cancelAtPeriodEnd);
            hasSubscriptionUpdated = true;
        }

        if(planId != null && !planId.equals(subscription.getPlan().getId())) {
            Plan newPlan = planRepository.findById(planId)
                    .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", planId));
            subscription.setPlan(newPlan);
            hasSubscriptionUpdated = true;
        }

        if(hasSubscriptionUpdated) {
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public void cancelSubscription(String subscriptionId) {
        Subscription subscription = subscriptionRepository.findByStripeSubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", subscriptionId));
        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd) {
        Subscription subscription = subscriptionRepository.findByStripeSubscriptionId(subId).orElseThrow(() ->
                new ResourceNotFoundException("Subscription", "id", subId));

        Instant newStart = periodStart != null ? periodStart : subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE || subscription.getStatus() == SubscriptionStatus.INCOMPLETE) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

        subscriptionRepository.save(subscription);
    }

    @Override
    public void markSubscriptionPastDue(String subId) {
        Subscription subscription = subscriptionRepository.findByStripeSubscriptionId(subId).orElseThrow(() ->
                new ResourceNotFoundException("Subscription", "id", subId));

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE) {
            return;
        }
        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);
        //TODO Email Notification

    }

    @Override
    public boolean canCreateNewProject() {
        Long userId = jwtUtils.getCurrentUserId();
        SubscriptionResponse currentSubscription = getCurrentSubscription();

        int countOfOwnedProjects = projectMemberRepository.countProjectOwnedByUser(userId);

        if(currentSubscription.plan() == null) {
            int FREE_TIER_PROJECTS_ALLOWED = 1;
            return countOfOwnedProjects < FREE_TIER_PROJECTS_ALLOWED;
        }

        return countOfOwnedProjects < currentSubscription.plan().maxProjects();
    }
}
