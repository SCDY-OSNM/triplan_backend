package scdy.couponservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scdy.couponservice.entity.Coupon;
import scdy.couponservice.entity.QCoupon;
import scdy.couponservice.exception.CouponNotFoundException;

@RequiredArgsConstructor
@Repository
public class CouponCustomRepositoryImpl implements CouponCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QCoupon coupon = QCoupon.coupon;

    @Override
    public Coupon findByIdOrElseThrow(Long couponId) {
        Coupon result = queryFactory
                .select(coupon)
                .where(coupon.couponId.eq(couponId))
                .fetchFirst();

        if(result == null) {
            throw new CouponNotFoundException("존재하지 않는 쿠폰입니다.");
        }

        return result;
    }
}
