package scdy.couponservice.repository;

import scdy.couponservice.entity.Coupon;

public interface CouponCustomRepository {

    Coupon findByIdWithPessimisticLock(Long couponId);

    Coupon findByIdOrElseThrow(Long couponId);
}
