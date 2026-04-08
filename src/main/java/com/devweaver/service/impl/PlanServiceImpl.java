package com.devweaver.service.impl;

import com.devweaver.dto.plan.PlanResponse;
import com.devweaver.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    @Override
    public List<PlanResponse> getAllActivePlans() {
        return null;
    }
}
