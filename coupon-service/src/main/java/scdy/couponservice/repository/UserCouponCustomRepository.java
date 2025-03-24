package scdy.couponservice.repository;

import scdy.couponservice.entity.UserCoupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponCustomRepository {

    List<UserCoupon> getUserCouponListByUserId(Long userId);

    Long getUserCouponCountByCouponId(Long couponId);

    Optional<UserCoupon> findUserCouponByUserIdAndCouponId(Long userId, Long couponId);

    UserCoupon findByIdOrElseThrow(Long userCouponId);
}
