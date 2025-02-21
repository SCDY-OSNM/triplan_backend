package scdy.planservice.repository.queryDsl;

import scdy.planservice.entity.PlanDetail;

import java.util.List;

public interface PlanDetailCustomRepository {
    List<PlanDetail> findByPlanId(Long planId);
}
