package eu.money.exception;

public abstract class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

    public CustomException() {
        super();
    }

    public abstract String getCode();

    public abstract int getHttpStatus();

}
