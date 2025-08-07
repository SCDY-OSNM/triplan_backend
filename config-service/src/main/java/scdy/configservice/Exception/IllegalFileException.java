package scdy.configservice.Exception;

import scdy.configservice.common.exceptions.BadRequestException;

public class IllegalFileException extends BadRequestException {
    public IllegalFileException(String message) {
        super(message);
    }
}
