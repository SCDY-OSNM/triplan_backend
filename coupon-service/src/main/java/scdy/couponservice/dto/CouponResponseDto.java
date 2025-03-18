package scdy.couponservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.couponservice.entity.Coupon;
import scdy.couponservice.enums.CouponType;
import scdy.couponservice.enums.DiscountType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CouponResponseDto {

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

    @Builder
    public CouponResponseDto(Long couponId, String couponName, Long couponCode, CouponType couponType, DiscountType discountType,
                             LocalDate issueDate, LocalDate expiryDate, Boolean available, Integer couponMinimum,
                             Integer couponMaximum, Integer discountPrice, Integer discountPercentage, Integer couponAmount) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponCode = couponCode;
        this.couponType = couponType;
        this.discountType = discountType;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.available = available;
        this.couponMinimum = couponMinimum;
        this.couponMaximum = couponMaximum;
        this.discountPrice = discountPrice;
        this.discountPercentage = discountPercentage;
        this.couponAmount = couponAmount;
    }


    public static CouponResponseDto from(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getCouponId(),
                coupon.getCouponName(),
                coupon.getCouponCode(),
                coupon.getCouponType(),
                coupon.getDiscountType(),
                coupon.getIssueDate(),
                coupon.getExpiryDate(),
                coupon.getAvailable(),
                coupon.getCouponMinimum(),
                coupon.getCouponMaximum(),
                coupon.getDiscountPrice(),
                coupon.getDiscountPercentage(),
                coupon.getCouponAmount()
        );
    }
}
