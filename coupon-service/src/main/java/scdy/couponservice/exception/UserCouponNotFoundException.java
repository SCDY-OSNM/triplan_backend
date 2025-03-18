package scdy.couponservice.exception;

import scdy.couponservice.common.exceptions.NotFoundException;

public class UserCouponNotFoundException extends NotFoundException {
    public UserCouponNotFoundException(String message) {
        super(message);
    }
}
