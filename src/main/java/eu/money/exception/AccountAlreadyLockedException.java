package eu.money.exception;

import eu.money.constant.ErrorCode;
import org.eclipse.jetty.http.HttpStatus;

public class AccountAlreadyLockedException extends CustomException {

    public AccountAlreadyLockedException(String message) {
        super(message);
    }
    public AccountAlreadyLockedException() {
        super();
    }

    @Override
    public String getCode() {
        return ErrorCode.ACCOUNT_ALREADY_LOCKED.name();
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.BAD_REQUEST_400;
    }
}
