package scdy.planservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scdy.planservice.common.exceptions.NotFoundException;
import scdy.planservice.entity.Plan;
import scdy.planservice.enums.Place;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query("select p from Plan p where p.planId = :planId")
    Optional<Plan> findById(Long planId);

    @Query("select p from Plan p where p.userId = :userId")
    List<Plan> findByUserId(Long userId);

    @Query("select p from Plan p where p.planPlace = :place")
    List<Plan> findByPlace(Place place);
    default Plan findByIdOrElseThrow(Long planId){
        return findById(planId).orElseThrow(()-> new NotFoundException("Plan Not Exist"));
    }

}
