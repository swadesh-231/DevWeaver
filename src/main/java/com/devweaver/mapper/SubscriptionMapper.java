package com.devweaver.mapper;

import com.devweaver.dto.plan.PlanResponse;
import com.devweaver.dto.plan.SubscriptionResponse;
import com.devweaver.entity.Plan;
import com.devweaver.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);
}
