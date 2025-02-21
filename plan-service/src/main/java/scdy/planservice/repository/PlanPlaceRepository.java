package scdy.planservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scdy.planservice.entity.PlanPlace;
import scdy.planservice.enums.Place;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PlanPlaceRepository extends JpaRepository<PlanPlace, Long> {
    @Query("select pp from PlanPlace pp where pp.planId = :planId")
    List<PlanPlace> findPlanPlaceByPlanId(Long planId);

    Optional<PlanPlace> findPlanPlaceById(Long planPlaceId);

    @Query("select pp from PlanPlace pp where pp.place =: place")
    List<PlanPlace> findPlanPlaceByPlace(Place place);

}
