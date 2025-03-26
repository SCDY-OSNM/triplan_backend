package scdy.couponservice.repository;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import scdy.couponservice.entity.QUserCoupon;
import scdy.couponservice.entity.UserCoupon;
import scdy.couponservice.exception.UserCouponNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserCouponCustomRepositoryImpl implements UserCouponCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QUserCoupon userCoupon = QUserCoupon.userCoupon;

    @Override
    public List<UserCoupon> getUserCouponListByUserId(Long userId) {
        return queryFactory
                .selectFrom(userCoupon)
                .where(userCoupon.userId.eq(userId))
                .fetch();
    }

    @Override
    public Long getUserCouponCountByCouponId(Long couponId) {
        Long num = queryFactory
                .select(Wildcard.count)
                .from(userCoupon)
                .where(userCoupon.coupon.couponId.eq(couponId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
        log.info("발급된 쿠폰 수 :{}", num.toString());
        return num;
    }

    @Override
    public Optional<UserCoupon> findUserCouponByUserIdAndCouponId(Long userId, Long couponId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(userCoupon)
                .where(userCoupon.userId.eq(userId), userCoupon.coupon.couponId.eq(couponId))
                .fetchFirst());
    }

    @Override
    public UserCoupon findByIdOrElseThrow(Long userCouponId) {
        UserCoupon result = queryFactory
                .select(userCoupon)
                .where(userCoupon.userCouponId.eq(userCouponId))
                .fetchOne();

        if (result == null) {
            throw new UserCouponNotFoundException("존재하지 않는 사용자 쿠폰입니다.");
        }
        return result;
    }
}
