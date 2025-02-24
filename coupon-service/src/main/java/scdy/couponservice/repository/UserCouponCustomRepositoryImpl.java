package scdy.couponservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scdy.couponservice.entity.QUserCoupon;
import scdy.couponservice.entity.UserCoupon;
import scdy.couponservice.exception.UserCouponNotFoundException;

import java.util.List;

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
        return queryFactory
                .selectFrom(userCoupon)
                .where(userCoupon.coupon.couponId.eq(couponId))
                .fetch().stream().count();
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
