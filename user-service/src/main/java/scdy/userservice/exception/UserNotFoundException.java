package scdy.userservice.exception;

import scdy.userservice.common.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message){
        super(message);
    }
}
