package com.devweaver.service;

import com.devweaver.dto.plan.PlanLimitsResponse;
import com.devweaver.dto.plan.UsageTodayResponse;

public interface PlanUseService {
    UsageTodayResponse getTodayUsageOfUser(Long userId);
    PlanLimitsResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
