package eu.money.exception;

import eu.money.constant.ErrorCode;
import org.eclipse.jetty.http.HttpStatus;

public class NotEnoughBalanceException extends CustomException {
    public NotEnoughBalanceException(String message) {
        super(message);
    }
    public NotEnoughBalanceException() {
        super();
    }

    @Override
    public String getCode() {
        return ErrorCode.NOT_ENOUGH_BALANCE.name();
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.BAD_REQUEST_400;
    }
}
