package scdy.reviewservice.exception;

import scdy.reviewservice.common.exceptions.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
