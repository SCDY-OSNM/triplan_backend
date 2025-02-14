package scdy.reservationservice.exception;

import scdy.reservationservice.common.exceptions.ForbiddenException;

public class ReservationForbiddenException extends ForbiddenException {
    public ReservationForbiddenException(String message) {
        super(message);
    }
}
