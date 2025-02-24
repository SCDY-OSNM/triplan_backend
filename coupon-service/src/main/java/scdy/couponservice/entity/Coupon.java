package scdy.couponservice.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import scdy.couponservice.enums.CouponType;
import scdy.couponservice.enums.DiscountType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(nullable = false)
    private String couponName;

    @Column(nullable = false)
    private Long couponCode;

    @Column(nullable = false)
    private CouponType couponType;

    @Column(nullable = false)
    private DiscountType discountType;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private Boolean available;

    //최소 주문금액
    @Column(nullable = false)
    private Integer couponMinimum;

    //최대 할인금액
    @Column(nullable = false)
    private Integer couponMaximum;

    //할인금액
    private Integer discountPrice;

    //할인 비율
    private Integer discountPercentage;

    //선착순 쿠폰 발급 개수
    private Integer couponAmount;

    @Builder
    public Coupon(String couponName, Long couponCode, CouponType couponType, DiscountType discountType,
                  LocalDate issueDate, LocalDate expiryDate, Boolean available, Integer couponMinimum,
                  Integer couponMaximum, Integer discountPrice, Integer discountPercentage, Integer couponAmount) {
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

    public void updateCoupon(String couponName, LocalDate expiryDate, Boolean available){
        this.couponName = couponName;
        this.expiryDate = expiryDate;
        this.available = available;
    }
}
