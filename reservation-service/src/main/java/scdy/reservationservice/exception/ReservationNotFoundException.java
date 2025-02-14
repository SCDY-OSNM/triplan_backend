package scdy.reservationservice.exception;

import scdy.reservationservice.common.exceptions.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException(String message) {
            super(message);
        }

}
