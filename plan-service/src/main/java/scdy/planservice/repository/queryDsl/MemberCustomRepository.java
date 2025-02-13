package scdy.planservice.repository.queryDsl;

import scdy.planservice.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberCustomRepository {
    List<Member> findByPlanId(Long planId);

    Optional<Member> findByUserIdPlanId(Long userId, Long planId);
}
