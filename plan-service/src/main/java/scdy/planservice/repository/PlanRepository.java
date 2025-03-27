package scdy.planservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.planservice.entity.Plan;
import scdy.planservice.exception.PlanNotFoundException;
import scdy.planservice.repository.queryDsl.PlanCustomRepository;

public interface PlanRepository extends JpaRepository<Plan, Long>, PlanCustomRepository {

    default Plan findByIdOrElseThrow(Long planId){
        return findById(planId).orElseThrow(()-> new PlanNotFoundException("플랜을 찾을 수 없습니다."));
    }

}
