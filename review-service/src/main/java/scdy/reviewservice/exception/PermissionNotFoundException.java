package scdy.reviewservice.exception;

import scdy.reviewservice.common.exceptions.ForbiddenException;

public class PermissionNotFoundException extends ForbiddenException {
    public PermissionNotFoundException(String message) {
        super(message);
    }
}
