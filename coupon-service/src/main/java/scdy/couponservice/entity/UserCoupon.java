package scdy.couponservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCouponId;

    private Long userId;

    private Boolean isUsed;

    @JoinColumn(name = "coupon_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Coupon coupon;


    @Builder
    public UserCoupon(Coupon coupon,
                      Boolean isUsed,
                      Long userId) {
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.userId = userId;
    }

    public void updateUserCoupon(Boolean isUsed) {
        this.isUsed = isUsed;
    }
}
