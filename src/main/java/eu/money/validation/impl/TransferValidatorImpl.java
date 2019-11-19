package eu.money.validation.impl;

import eu.money.model.Transfer;
import eu.money.validation.Validator;

import java.util.ArrayList;
import java.util.List;

public class TransferValidatorImpl extends Validator<Transfer> {

    @Override
    public void validate(Transfer transfer) {
        List<String> validationExceptions = new ArrayList<>();
        if (transfer.getAmount() == null || transfer.getAmount().signum() != 1) {
            validationExceptions.add("Amount should be bigger than zero");
        }
        processValidationExceptions(validationExceptions);
    }

}
