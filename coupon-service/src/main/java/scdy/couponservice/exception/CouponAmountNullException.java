package scdy.couponservice.exception;

import scdy.couponservice.common.exceptions.BadRequestException;

public class CouponAmountNullException extends BadRequestException {
    public CouponAmountNullException(String message) {
        super(message);
    }
}
