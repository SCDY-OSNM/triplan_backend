package scdy.planservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scdy.planservice.common.exceptions.NotFoundException;
import scdy.planservice.entity.Plan;
import scdy.planservice.enums.Place;
import scdy.planservice.repository.queryDsl.PlanCustomRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long>, PlanCustomRepository {

    //@Query("select p from Plan p where p.userId = :userId")
    //List<Plan> findByUserId(Long userId);
    // userId는 생성자 아이디임. 그룹원들이 자기 플랜 조회할 수 있게 해야됨

    default Plan findByIdOrElseThrow(Long planId){
        return findById(planId).orElseThrow(()-> new NotFoundException("Plan Not Exist"));
    }

}
