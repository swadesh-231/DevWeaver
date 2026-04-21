package com.devweaver.controller;

import com.devweaver.dto.plan.*;
import com.devweaver.security.jwt.JwtUtils;
import com.devweaver.service.PlanService;
import com.devweaver.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SubscriptionController {
    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    private final JwtUtils jwtUtils;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription() {
        Long userId = jwtUtils.getCurrentUserId();
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping("/api/stripe/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(
            @Valid @RequestBody CheckoutRequest request
    ) {
        Long userId = jwtUtils.getCurrentUserId();
        return ResponseEntity.ok(subscriptionService.createCheckoutSessionUrl(request, userId));
    }

    @PostMapping("/api/stripe/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal() {
        Long userId = jwtUtils.getCurrentUserId();
        return ResponseEntity.ok(subscriptionService.openCustomerPortal(userId));
    }
}
