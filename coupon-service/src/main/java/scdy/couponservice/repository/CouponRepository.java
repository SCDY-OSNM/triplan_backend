package scdy.couponservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.couponservice.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponCustomRepository {

}
