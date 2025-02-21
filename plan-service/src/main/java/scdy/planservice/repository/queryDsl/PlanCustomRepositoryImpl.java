package scdy.planservice.repository.queryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scdy.planservice.entity.Plan;
import scdy.planservice.entity.QPlan;
import scdy.planservice.enums.Place;

import java.util.List;

import static scdy.planservice.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class PlanCustomRepositoryImpl implements PlanCustomRepository{
    private final JPAQueryFactory queryFactory;

    QPlan plan = QPlan.plan;

    @Override
    public List<Plan> findByUserId(Long userId){
        return queryFactory
                .select(plan)
                .where(plan.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<Plan> findByPlace(Place place){
        return queryFactory
                .select(plan)
                .where(plan.planPlace.contains(place))
                .fetch();
    }

    @Override
    public List<Plan> findByUserIdInMember(Long userId){
        return queryFactory
                .select(member.plan)
                .from(member)
                .join(member.plan, plan) // queryDSL이 ManyToOne 필드를 자동 조인하지 않음.
                .where(member.userId.eq(userId))
                .fetch();
    }
}
