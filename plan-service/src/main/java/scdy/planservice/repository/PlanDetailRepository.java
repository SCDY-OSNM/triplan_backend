package scdy.planservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.planservice.entity.PlanDetail;
import scdy.planservice.exception.PlanDetailNotFoundException;
import scdy.planservice.repository.queryDsl.PlanDetailCustomRepository;

public interface PlanDetailRepository extends JpaRepository<PlanDetail, Long> , PlanDetailCustomRepository {
    // @Query("select pd from PlanDetail pd where pd.planDetailId = :planDetailId")
    //Optional<PlanDetail> findByPlanDetailId(Long planDetailId);

    default PlanDetail findByPlanDetailIdOrElseThrow(Long planId){
        return findById(planId).orElseThrow(()-> new PlanDetailNotFoundException("플랜 디테일을 찾을 수 없습니다."));
    }
}
