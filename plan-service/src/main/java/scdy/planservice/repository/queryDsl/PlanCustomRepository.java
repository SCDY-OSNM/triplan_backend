package scdy.planservice.repository.queryDsl;

import scdy.planservice.entity.Plan;
import scdy.planservice.enums.Place;

import java.util.List;

public interface PlanCustomRepository {
    List<Plan> findByUserId(Long userId);

    List<Plan> findByPlace(Place place);

    List<Plan> findByUserIdInMember(Long userId);
}
