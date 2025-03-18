package scdy.couponservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.couponservice.entity.UserCoupon;

@Getter
@NoArgsConstructor
public class UserCouponResponseDto {
    private Long userCouponId;

    private Long userId;

    private Boolean isUsed;

    private Long couponId;

    @Builder
    public UserCouponResponseDto(Long userCouponId,
                                 Long userId,
                                 Boolean isUsed,
                                 Long couponId) {
        this.userCouponId = userCouponId;
        this.userId = userId;
        this.isUsed = isUsed;
        this.couponId = couponId;
    }

    public static UserCouponResponseDto from(UserCoupon userCoupon) {
        return new UserCouponResponseDto(
                userCoupon.getUserCouponId(),
                userCoupon.getUserId(),
                userCoupon.getIsUsed(),
                userCoupon.getCoupon().getCouponId()
        );
    }

}
