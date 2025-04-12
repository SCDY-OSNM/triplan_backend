package scdy.configservice.Exception;

import scdy.configservice.common.exceptions.BadRequestException;

public class S3Exception extends BadRequestException {
    public S3Exception(String message) {
        super(message);
    }
}
