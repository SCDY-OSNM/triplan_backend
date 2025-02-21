package scdy.couponservice.exception;

import scdy.couponservice.common.exceptions.BadRequestException;

public class CouponAmountErrorException extends BadRequestException {
    public CouponAmountErrorException(String message) {
        super(message);
    }
}
