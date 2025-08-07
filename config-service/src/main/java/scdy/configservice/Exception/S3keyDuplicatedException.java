package scdy.configservice.Exception;

import scdy.configservice.common.exceptions.BadRequestException;

public class S3keyDuplicatedException extends BadRequestException {
    public S3keyDuplicatedException(String message) {
        super(message);
    }
}
