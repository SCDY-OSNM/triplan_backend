package scdy.reservationservice.exception;

import scdy.reservationservice.common.exceptions.ForbiddenException;

public class NotfoundPermissionException extends ForbiddenException{
    public NotfoundPermissionException(String message) {
        super(message);
    }
}
