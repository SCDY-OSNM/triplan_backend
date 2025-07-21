package scdy.configservice.Exception;

import scdy.configservice.common.exceptions.BadRequestException;

public class LackOfImageInfoException extends BadRequestException {
    public LackOfImageInfoException(String message) {
        super(message);
    }
}
