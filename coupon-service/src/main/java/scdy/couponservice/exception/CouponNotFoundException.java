package scdy.couponservice.exception;

import scdy.couponservice.common.exceptions.NotFoundException;

public class CouponNotFoundException extends NotFoundException {
    public CouponNotFoundException(String message) {
        super(message);
    }
}
