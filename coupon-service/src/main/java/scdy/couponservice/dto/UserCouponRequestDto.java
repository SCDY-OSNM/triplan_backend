package scdy.couponservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.couponservice.entity.Coupon;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponRequestDto {

    private Long userCouponId;

    private Long userId;

    private Boolean isUsed;

    private Long couponId;
}
