package eu.money.exception;

import eu.money.constant.ErrorCode;
import org.eclipse.jetty.http.HttpStatus;

public class NonUniqueResultException extends CustomException {
    public NonUniqueResultException(String message) {
        super(message);
    }
    public NonUniqueResultException() {
        super();
    }

    @Override
    public String getCode() {
        return ErrorCode.NON_UNIQUE_RESULT.name();
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR_500;
    }
}
