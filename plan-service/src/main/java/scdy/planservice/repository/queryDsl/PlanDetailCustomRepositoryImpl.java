package scdy.planservice.repository.queryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scdy.planservice.entity.PlanDetail;
import scdy.planservice.entity.QPlanDetail;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlanDetailCustomRepositoryImpl implements PlanDetailCustomRepository{
    private final JPAQueryFactory queryFactory;

    QPlanDetail planDetail = QPlanDetail.planDetail;

    @Override
    public List<PlanDetail> findByPlanId(Long planId){
        return queryFactory
                .select(planDetail)
                .where(planDetail.plan.planId.eq(planId))
                .fetch();
    }
}
