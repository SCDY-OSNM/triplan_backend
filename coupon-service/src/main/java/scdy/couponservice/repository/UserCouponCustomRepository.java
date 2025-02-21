package scdy.couponservice.repository;

import scdy.couponservice.entity.UserCoupon;

import java.util.List;

public interface UserCouponCustomRepository {

    List<UserCoupon> getUserCouponListByUserId(Long userId);

    Long getUserCouponCountByCouponId(Long couponId);

    UserCoupon findByIdOrElseThrow(Long userCouponId);
}
