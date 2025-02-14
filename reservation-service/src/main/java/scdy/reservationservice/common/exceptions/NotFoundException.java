package scdy.reservationservice.common.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;


public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}