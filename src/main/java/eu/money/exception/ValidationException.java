package eu.money.exception;

import eu.money.constant.ErrorCode;
import org.eclipse.jetty.http.HttpStatus;

public class ValidationException extends CustomException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException() {
        super();
    }

    @Override
    public String getCode() {
        return ErrorCode.REQUEST_VALIDATION_FAILED.name();
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.BAD_REQUEST_400;
    }
}