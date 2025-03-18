package scdy.couponservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.couponservice.enums.CouponType;
import scdy.couponservice.enums.DiscountType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequestDto {

    private Long couponId;

    private String couponName;

    private Long couponCode;

    private CouponType couponType;

    private DiscountType discountType;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private Boolean available;

    private Integer couponMinimum;

    private Integer couponMaximum;

    private Integer discountPrice;

    private Integer discountPercentage;

    private Integer couponAmount;
}
