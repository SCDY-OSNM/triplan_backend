package scdy.configservice.Exception;

import scdy.configservice.common.exceptions.BadRequestException;

public class FileSizeOverException extends BadRequestException {
    public FileSizeOverException(String message) {
        super(message);
    }
}
