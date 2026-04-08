package com.devweaver.service;

import com.devweaver.dto.plan.PlanResponse;

import java.util.List;

public interface PlanService {
    List<PlanResponse> getAllActivePlans();
}
