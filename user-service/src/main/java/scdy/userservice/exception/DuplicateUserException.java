package scdy.userservice.exception;

import scdy.userservice.common.exceptions.BadRequestException;

public class DuplicateUserException extends BadRequestException {
    public DuplicateUserException(String message){
        super(message);
    }
}
