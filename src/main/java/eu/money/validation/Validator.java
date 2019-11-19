package eu.money.validation;

import eu.money.exception.ValidationException;

import java.util.List;

public abstract class Validator<T> {

    public abstract void validate(T transfer);

    protected void processValidationExceptions(List<String> validationExceptions) {
        if (validationExceptions.size() > 0) {
            throw new ValidationException(String.join(";", validationExceptions));
        }
    }
}
