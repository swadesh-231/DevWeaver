package com.devweaver.controller;

import com.devweaver.dto.plan.PlanLimitsResponse;
import com.devweaver.dto.plan.UsageTodayResponse;
import com.devweaver.security.jwt.JwtUtils;
import com.devweaver.service.PlanUseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usage")
public class PlanUseController {
    private final PlanUseService planUseService;
    private final JwtUtils jwtUtils;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage() {
        Long userId = jwtUtils.getCurrentUserId();
        return ResponseEntity.ok(planUseService.getTodayUsageOfUser(userId));
    }

    @GetMapping("/limits")
    public ResponseEntity<PlanLimitsResponse> getPlanLimits() {
        Long userId = jwtUtils.getCurrentUserId();
        return ResponseEntity.ok(planUseService.getCurrentSubscriptionLimitsOfUser(userId));
    }
}
