package scdy.planservice.repository.queryDsl;

import com.querydsl.jpa.JPAExpressions;
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
                .selectFrom(plan)
                .where(plan.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<Plan> findByPlace(Place place){
        return queryFactory
                .selectFrom(plan)
                .where(plan.planPlace.contains(place))
                .fetch();
    }

    @Override
    public List<Plan> findByUserIdInMember(Long userId){
        return queryFactory
                .selectFrom(plan)
                .where(plan.planId.in(
                        JPAExpressions
                                .select(member.planId)
                                .from(member)
                                .where(member.userId.eq(userId))
                ))
                .fetch();
    } // 현재 유저 아이디를 가지고 있는 멤버의 플랜 아이디로 플랜을 가져옴
    // 대량의 데이터 조회시 join이 더 적절할 수 있음
}
