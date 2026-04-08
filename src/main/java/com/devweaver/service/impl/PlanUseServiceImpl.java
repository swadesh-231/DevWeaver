package com.devweaver.service.impl;

import com.devweaver.dto.plan.PlanLimitsResponse;
import com.devweaver.dto.plan.UsageTodayResponse;
import com.devweaver.service.PlanUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanUseServiceImpl implements PlanUserService {
    @Override
    public UsageTodayResponse getTodayUsageOfUser(Long userId) {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionLimitsOfUser(Long userId) {
        return null;
    }
}
