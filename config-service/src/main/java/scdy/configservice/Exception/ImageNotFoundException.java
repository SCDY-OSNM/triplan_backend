package scdy.configservice.Exception;

import scdy.configservice.common.exceptions.NotFoundException;

public class ImageNotFoundException extends NotFoundException {
    public ImageNotFoundException(String message) {
        super(message);
    }
}
