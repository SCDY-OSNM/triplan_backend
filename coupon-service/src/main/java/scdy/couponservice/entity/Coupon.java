package scdy.couponservice.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.couponservice.enums.CouponType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
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
    private LocalDate expiryDate;

    @Column(nullable = false)
    private Boolean available;

    @Column(nullable = false)
    private Integer couponMinimum;


    @Builder
    public Coupon(String couponName,
                  Long couponCode,
                  CouponType couponType,
                  LocalDate expiryDate,
                  Boolean available,
                  Integer couponMinimum) {
        this.couponName = couponName;
        this.couponCode = couponCode;
        this.couponType = couponType;
        this.expiryDate = expiryDate;
        this.available = available;
        this.couponMinimum = couponMinimum;
    }
}
