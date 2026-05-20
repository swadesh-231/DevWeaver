package com.devweaver.service.impl;

import com.devweaver.dto.plan.CheckoutRequest;
import com.devweaver.dto.plan.CheckoutResponse;
import com.devweaver.dto.plan.PortalResponse;
import com.devweaver.entity.Plan;
import com.devweaver.entity.User;
import com.devweaver.exception.ResourceNotFoundException;
import com.devweaver.repository.PlanRepository;
import com.devweaver.repository.UserRepository;
import com.devweaver.security.jwt.JwtUtils;
import com.devweaver.service.PaymentProcessor;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripePaymentProcess implements PaymentProcessor {
    private final JwtUtils jwtUtils;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;

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
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripe_price_id()).setQuantity(1L).build())
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
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata) {
        log.info("Received Webhook Event");

    }
}
