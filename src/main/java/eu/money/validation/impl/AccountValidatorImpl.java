package eu.money.validation.impl;

import eu.money.model.Account;
import eu.money.validation.Validator;

import java.util.ArrayList;
import java.util.List;

public class AccountValidatorImpl extends Validator<Account> {

    @Override
    public void validate(Account account) {
        List<String> validationExceptions = new ArrayList<>();
        if (account.getOwner() != null && account.getOwner().length() > 200) {
            validationExceptions.add("Owner should be longer than 200 characters.");
        }
        if (account.getIban() != null && account.getIban().length() > 34) {
            validationExceptions.add("IBAN should be longer than 34 characters.");
        }

        processValidationExceptions(validationExceptions);
    }

}