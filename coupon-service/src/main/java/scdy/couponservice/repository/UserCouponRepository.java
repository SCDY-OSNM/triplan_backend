package scdy.couponservice.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import scdy.couponservice.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long>, UserCouponCustomRepository {

//    @Query(value = "SELECT COUNT(c.coupon_id) FROM coupon c WHERE c.coupon_id = :couponId FOR UPDATE;", nativeQuery = true)
//    Long getUserCouponCountByCouponId(@Param("couponId") Long couponId);
}
