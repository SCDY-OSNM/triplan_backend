package scdy.couponservice.repository;

import scdy.couponservice.entity.Coupon;

public interface CouponCustomRepository {



    Coupon findByIdOrElseThrow(Long couponId);
}
