package eu.money.validation.impl;

import eu.money.exception.ValidationException;
import eu.money.model.Account;
import eu.money.model.Transfer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

public class TransferValidatorImplTest {

    private TransferValidatorImpl transferValidatorTest = new TransferValidatorImpl();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldValidateSuccessfully() {
        // when
        transferValidatorTest.validate(new Transfer(new BigDecimal(1)));

        // then no Exception is thrown
    }

    @Test
    public void shouldThrowExceptionThatAmountIsNull() {
        // given
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Amount should be bigger than zero");

        // when
        transferValidatorTest.validate(new Transfer(new BigDecimal(-1)));

        // then

    }

    @Test
    public void shouldThrowExceptionThatAmountIsLessThanZero() {
        // given
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Amount should be bigger than zero");

        // when
        transferValidatorTest.validate(new Transfer(null));

    }

}
