package eu.money.validation.impl;

import eu.money.exception.ValidationException;
import eu.money.model.Account;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AccountValidatorImplTest {

    private AccountValidatorImpl accountValidator = new AccountValidatorImpl();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldValidateSuccessfully() {
        // when
        accountValidator.validate(new Account("test", "owner"));

        // then no Exception is thrown
    }

    @Test
    public void shouldThrowExceptionThatIBANIsTooLong() {
        // given
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("IBAN should be longer than 34 characters.");

        // when
        accountValidator.validate(new Account(RandomStringUtils.random(35), "owner"));
    }

    @Test
    public void shouldThrowExceptionThatOwnerIsTooLong() {
        // given
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Owner should be longer than 200 characters.");

        // when
        accountValidator.validate(new Account(RandomStringUtils.random(32), RandomStringUtils.random(201)));
    }


}
