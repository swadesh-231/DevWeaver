package com.devweaver.service;

import com.devweaver.dto.plan.CheckoutRequest;
import com.devweaver.dto.plan.CheckoutResponse;
import com.devweaver.dto.plan.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {
    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);
    PortalResponse openCustomerPortal(Long userId);
    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
