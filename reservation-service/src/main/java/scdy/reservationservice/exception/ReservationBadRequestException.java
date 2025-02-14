package scdy.reservationservice.exception;

import scdy.reservationservice.common.exceptions.BadRequestException;

public class ReservationBadRequestException extends BadRequestException {
    public ReservationBadRequestException(String message) {
        super(message);
    }
}
