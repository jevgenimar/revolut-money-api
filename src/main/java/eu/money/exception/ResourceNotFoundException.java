package eu.money.exception;

import eu.money.constant.ErrorCode;
import org.eclipse.jetty.http.HttpStatus;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException() {
        super();
    }

    @Override
    public String getCode() {
        return ErrorCode.RESOURCE_NOT_FOUND.name();
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.NOT_FOUND_404;
    }
}
