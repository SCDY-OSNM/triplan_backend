package scdy.planservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.planservice.entity.PlanPlace;
import scdy.planservice.enums.Place;

import java.util.List;

public interface PlanPlaceRepository extends JpaRepository<PlanPlace, Long> {
    List<PlanPlace> findPlanPlaceByPlanId(Long planId);

    //Optional<PlanPlace> findPlanPlaceById(Long planPlaceId);

    List<PlanPlace> findPlanPlaceByPlace(Place place);

}
