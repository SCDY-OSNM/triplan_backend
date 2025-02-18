package scdy.planservice.exception;

import scdy.planservice.common.exceptions.NotFoundException;

public class PlanNotFoundException extends NotFoundException {
    public PlanNotFoundException(String message) {
        super(message);
    }
}
