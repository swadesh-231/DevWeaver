package com.devweaver.service.impl;

import com.devweaver.dto.plan.CheckoutRequest;
import com.devweaver.dto.plan.CheckoutResponse;
import com.devweaver.dto.plan.PortalResponse;
import com.devweaver.entity.Plan;
import com.devweaver.entity.User;
import com.devweaver.entity.enums.SubscriptionStatus;
import com.devweaver.exception.BadRequestException;
import com.devweaver.exception.ResourceNotFoundException;
import com.devweaver.repository.PlanRepository;
import com.devweaver.repository.UserRepository;
import com.devweaver.security.jwt.JwtUtils;
import com.devweaver.service.PaymentProcessor;
import com.devweaver.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripePaymentProcess implements PaymentProcessor {
    private final JwtUtils jwtUtils;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    @Value("${spring.client.url}")
    private String frontendUrl;
    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request) {
        Plan plan  = planRepository.findById(request.planId())
                .orElseThrow(()->new ResourceNotFoundException("Plan", "id", request.planId()));
        Long currentUserId = jwtUtils.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("User", "id", currentUserId));

        var params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        SessionCreateParams.SubscriptionData.builder()
                                .setBillingMode(
                                        SessionCreateParams.SubscriptionData.BillingMode.builder()
                                                .setType(
                                                        SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE
                                                )
                                                .build()
                                )
                                .build()
                )
                .setSuccessUrl(frontendUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "/cancel.html")
                .putMetadata("id",currentUserId.toString())
                .putMetadata("planId",plan.getId().toString());
        try {
            String stripeCustomerId = user.getStripe_customer_id();
            if (stripeCustomerId == null || stripeCustomerId.isEmpty()) {
                params.setCustomerEmail(user.getUsername());
            }else {
                params.setCustomer(stripeCustomerId);
            }
            Session session = Session.create(params.build());
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public PortalResponse openCustomerPortal() {
        Long userId = jwtUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
        String stripeCustomerId = user.getStripe_customer_id();
        if (stripeCustomerId == null || stripeCustomerId.isEmpty()) {
            throw  new BadRequestException("User Dont have stripe customer id");

        }
        try{
            var portalSession = Session.create(
                    SessionCreateParams.builder()
                            .setCustomerAccount(stripeCustomerId)
                            .setReturnUrl(frontendUrl)
                            .build()
            );
            return new PortalResponse(portalSession.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata) {
        log.debug("handling Webhook Event: {}",type);
        switch (type) {
            case "checkout.session.completed" -> handleChekOutSessionCompleted((Session) stripeObject,metadata);
            case "customer.subscription.updated" -> handleCustomerSubscriptionUpdated((Subscription) stripeObject);
            case "customer.subscription.deleted" -> handleCustomerSubscriptionDeleted((Subscription) stripeObject);
            case "invoice.paid" -> handleInvoicePaid((Invoice) stripeObject);
            case "invoice.payment_failed" -> handlePaymentFailed((Invoice) stripeObject);
            default -> log.error("unknown event type: {}",type);
        }

    }
    private void handleChekOutSessionCompleted(Session session, Map<String, String> metadata) {
        Long useId = Long.parseLong(metadata.get("user_id"));
        Long planId = Long.parseLong(metadata.get("plan_id"));

        String subscriptionId = session.getSubscription();
        String customerId = session.getCustomer();

        User user  =  userRepository.findById(useId)
                .orElseThrow(()->new ResourceNotFoundException("User", "id", useId));

        if (user.getStripe_customer_id() == null || user.getStripe_customer_id().isEmpty()) {
            user.setStripe_customer_id(customerId);
            userRepository.save(user);
        }
        subscriptionService.activateSubscription(useId,planId,subscriptionId,customerId);
    }
    private void handleCustomerSubscriptionUpdated(Subscription subscription) {
        SubscriptionStatus status = mapStripeStatusToEnum(subscription.getStatus());
        if (status == null) {
            log.warn("Unknown status '{}' for subscription {}", subscription.getStatus(), subscription.getId());
            return;
        }

        SubscriptionItem item = subscription.getItems().getData().getFirst();
        Instant periodStart = toInstant(item.getCurrentPeriodStart());
        Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

        Long planId = resolvePlanId(item.getPrice());

        subscriptionService.updateSubscription(
                subscription.getId(), status, periodStart, periodEnd,
                subscription.getCancelAtPeriodEnd(), planId
        );

    }

    private void handleCustomerSubscriptionDeleted(Subscription subscription) {
        subscriptionService.cancelSubscription(subscription.getId());
    }
    private void handleInvoicePaid(Invoice invoice) {
        String subId = extractSubscriptionId(invoice);
        if(subId == null) return;
        try {
            Subscription subscription = Subscription.retrieve(subId);
            var item = subscription.getItems().getData().getFirst();
            Instant periodStart = toInstant(item.getCurrentPeriodStart());
            Instant periodEnd = toInstant(item.getCurrentPeriodEnd());
            subscriptionService.renewSubscriptionPeriod(
                    subId,
                    periodStart,
                    periodEnd
            );

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
    private void handlePaymentFailed(Invoice invoice) {
        String subId = extractSubscriptionId(invoice);
        if(subId == null) return;
        subscriptionService.markSubscriptionPastDue(subId);
    }
    private SubscriptionStatus mapStripeStatusToEnum(String status) {
        return switch (status) {
            case "active" -> SubscriptionStatus.ACTIVE;
            case "trialing" -> SubscriptionStatus.TRAILING;
            case "past_due", "unpaid", "paused", "incomplete_expired" -> SubscriptionStatus.PAST_DUE;
            case "canceled" -> SubscriptionStatus.CANCELED;
            case "incomplete" -> SubscriptionStatus.INCOMPLETE;
            default -> {
                log.warn("Unmapped Stripe status: {}", status);
                yield null;
            }
        };
    }
    private Instant toInstant(Long epoch) {
        return epoch != null ? Instant.ofEpochSecond(epoch) : null;
    }

    private Long resolvePlanId(Price price) {
        if (price == null || price.getId() == null) return null;
        return planRepository.findByStripePriceId(price.getId())
                .map(Plan::getId)
                .orElse(null);
    }

    private String extractSubscriptionId(Invoice invoice) {
        var parent = invoice.getParent();
        if (parent == null) return null;

        var subDetails = parent.getSubscriptionDetails();
        if (subDetails == null) return null;

        return subDetails.getSubscription();
    }
}
