package scdy.planservice.repository.queryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scdy.planservice.entity.Member;
import scdy.planservice.entity.QMember;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{
    private final JPAQueryFactory queryFactory;

    QMember member = QMember.member;

    @Override
    public List<Member> findByPlanId(Long planId) {
        return queryFactory
                .select(member)
                .where(member.plan.planId.eq(planId))
                .fetch();
    }

    @Override
    public Optional<Member> findByUserIdPlanId(Long userId, Long planId) {

        return Optional.ofNullable(
                queryFactory
                .select(member)
                .where(member.memberId.eq(planId)
                        .and(member.userId.eq(userId)))
                .fetchOne());
    }
}
