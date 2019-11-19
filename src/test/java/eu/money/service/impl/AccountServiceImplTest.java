package eu.money.service.impl;

import eu.money.model.Account;
import eu.money.repository.AccountRepository;
import eu.money.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceImplTest {

    private AccountService accountService;
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    public void shouldCreateAccount() {
        // given
        Account account = new Account("IBAN", "jevg.mar");
        when(accountRepository.save(account)).thenAnswer(invocation -> invocation.getArgument(0, Account.class));

        // when
        Account createdAccount = accountService.create(account);

        // then
        verify(accountRepository).save(account);
        assertThat(createdAccount.getIban(), equalTo(account.getIban()));
        assertThat(createdAccount.getOwner(), equalTo(account.getOwner()));
        assertThat(createdAccount.getBalance(), equalTo(new BigDecimal(0)));
        assertThat(createdAccount.getPublicId(), notNullValue());
    }

}
