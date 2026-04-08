package com.devweaver.service;

import com.devweaver.dto.plan.CheckoutRequest;
import com.devweaver.dto.plan.CheckoutResponse;
import com.devweaver.dto.plan.PortalResponse;
import com.devweaver.dto.plan.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);
    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId);
    PortalResponse openCustomerPortal(Long userId);
}
