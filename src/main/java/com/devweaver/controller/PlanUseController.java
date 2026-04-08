package com.devweaver.controller;

import com.devweaver.dto.plan.PlanLimitsResponse;
import com.devweaver.dto.plan.UsageTodayResponse;
import com.devweaver.service.PlanUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usage")
public class PlanUseController {
    private final PlanUserService planUserService;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage() {
        Long userId = 1L;
        return ResponseEntity.ok(planUserService.getTodayUsageOfUser(userId));
    }

    @GetMapping("/limits")
    public ResponseEntity<PlanLimitsResponse> getPlanLimits() {
        Long userId = 1L;
        return ResponseEntity.ok(planUserService.getCurrentSubscriptionLimitsOfUser(userId));
    }
}
