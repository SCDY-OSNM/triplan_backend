package scdy.planservice.exception;

import scdy.planservice.common.exceptions.NotFoundException;

public class PlanDetailNotFoundException extends NotFoundException {
    public PlanDetailNotFoundException(String message) {
        super(message);
    }
}
