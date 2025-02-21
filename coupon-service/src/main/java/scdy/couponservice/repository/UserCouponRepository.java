package scdy.couponservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.couponservice.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long>, UserCouponCustomRepository {

}
