package scdy.userservice.exception;

import scdy.userservice.common.exceptions.BadRequestException;

public class AlreadyDeletedException extends BadRequestException {
    public AlreadyDeletedException(String message){
        super(message);
    }
}
