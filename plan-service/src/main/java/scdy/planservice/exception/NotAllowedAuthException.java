package scdy.planservice.exception;

import scdy.planservice.common.exceptions.ForbiddenException;

public class NotAllowedAuthException extends ForbiddenException {
    public NotAllowedAuthException(String message) {
        super(message);
    }
}
