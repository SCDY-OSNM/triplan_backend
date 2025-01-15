package scdy.userservice.exception;

import scdy.userservice.common.exceptions.NotFoundException;

public class PasswordNotMatchException extends NotFoundException {
    public PasswordNotMatchException(String message){
        super(message);
    }
}
