package scdy.couponservice.exception;

import scdy.couponservice.common.exceptions.ForbiddenException;

public class PermissionNotfoundException extends ForbiddenException {
    public PermissionNotfoundException(String message) {
        super(message);
    }
}
