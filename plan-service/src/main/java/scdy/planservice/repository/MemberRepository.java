package scdy.planservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scdy.planservice.common.exceptions.NotFoundException;
import scdy.planservice.entity.Member;
import scdy.planservice.entity.Plan;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.memberId = :memberId")
    Optional<Member> findById(Long memberId);

    @Query("select m from Member m where m.plan.planId = :planId")
    List<Member> findByPlanId(Long planId);

    @Query("select m from Member m where m.userId = :userId and m.plan.planId = :planId")
    Optional<Member> findByUserIdAndPlanId(Long userId, Long planId); // QueryDSL적용

    default Member findByIdOrElseThrow(Long memberId){
        return findById(memberId).orElseThrow(()-> new NotFoundException("Member Not Exist"));
    }
}
