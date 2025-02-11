package scdy.planservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scdy.planservice.common.exceptions.NotFoundException;
import scdy.planservice.entity.PlanDetail;

import java.util.List;
import java.util.Optional;

public interface PlanDetailRepository extends JpaRepository<PlanDetail, Long> {
    @Query("select pd from PlanDetail pd where pd.planDetailId = :planDetailId")
    Optional<PlanDetail> findByPlanDetailId(Long planDetailId);


    List<PlanDetail> findByPlanId(Long planId);

    default PlanDetail findByPlanDetailIdOrElseThrow(Long planId){
        return findByPlanDetailId(planId).orElseThrow(()-> new NotFoundException("PlanDetail Not Found"));
    }
}
