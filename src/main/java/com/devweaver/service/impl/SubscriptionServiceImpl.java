package com.devweaver.service.impl;

import com.devweaver.dto.plan.CheckoutRequest;
import com.devweaver.dto.plan.CheckoutResponse;
import com.devweaver.dto.plan.PortalResponse;
import com.devweaver.dto.plan.SubscriptionResponse;
import com.devweaver.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        return null;
    }

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
