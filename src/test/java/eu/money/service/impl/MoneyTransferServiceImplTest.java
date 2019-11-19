package eu.money.service.impl;

import eu.money.configuration.DatabaseManager;
import eu.money.exception.NotEnoughBalanceException;
import eu.money.model.Transfer;
import eu.money.service.AccountService;
import eu.money.service.LockService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sql2o.Connection;
import org.sql2o.StatementRunnable;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class MoneyTransferServiceImplTest {

    private AccountService accountService = Mockito.mock(AccountService.class);
    private DatabaseManager databaseManager = Mockito.mock(DatabaseManager.class);
    private LockService lockService = Mockito.mock(LockService.class);
    private Connection connection = Mockito.mock(Connection.class);
    private MoneyTransferServiceImpl moneyTransferService;

    private static final String ACCOUNT_ID = "ID_12345";
    private static final String ACCOUNT_ID_2 = "ID_67890";

    private static final BigDecimal EIGHT = new BigDecimal(8);
    private static final BigDecimal TWENTY = new BigDecimal(20);
    private static final BigDecimal TWENTY_PLUS_EIGHT = new BigDecimal(28);
    private static final BigDecimal TWENTY_MINUS_EIGHT = new BigDecimal(12);
    private static final BigDecimal EIGHT_PLUS_EIGHT = new BigDecimal(16);

    @Before
    public void setUp() {
        moneyTransferService = new MoneyTransferServiceImpl(lockService, accountService, databaseManager);
    }

    @Test
    public void shouldDeposit() {
        // given
        when(accountService.getBalance(eq(ACCOUNT_ID), any())).thenReturn(EIGHT);
        mockRunInTranasctionAndInLock(ACCOUNT_ID);

        // when
        moneyTransferService.deposit(ACCOUNT_ID, new Transfer(TWENTY));

        // then
        Mockito.verify(accountService).getBalance(ACCOUNT_ID, connection);
        Mockito.verify(accountService).updateBalance(eq(ACCOUNT_ID), eq(TWENTY_PLUS_EIGHT), any());
    }

    @Test
    public void shouldWithdraw() {
        // given
        when(accountService.getBalance(eq(ACCOUNT_ID), any())).thenReturn(TWENTY);
        mockRunInTranasctionAndInLock(ACCOUNT_ID);

        // when
        moneyTransferService.withdraw(ACCOUNT_ID, new Transfer(EIGHT));

        // then
        Mockito.verify(accountService).getBalance(ACCOUNT_ID, connection);
        Mockito.verify(accountService).updateBalance(eq(ACCOUNT_ID), eq(TWENTY_MINUS_EIGHT), any());
    }

    @Test(expected = NotEnoughBalanceException.class)
    public void shouldThrowExceptionIfWithdrawnMoreThanBalance() {
        when(accountService.getBalance(eq(ACCOUNT_ID), any())).thenReturn(EIGHT);
        mockRunInTranasctionAndInLock(ACCOUNT_ID);
        // given
        moneyTransferService.withdraw(ACCOUNT_ID, new Transfer(TWENTY));
    }

    @Test
    public void shouldTransfer() {
        // given
        when(accountService.getBalance(eq(ACCOUNT_ID), any())).thenReturn(TWENTY);
        when(accountService.getBalance(eq(ACCOUNT_ID_2), any())).thenReturn(EIGHT);
        mockRunInTranasctionAndInLock(ACCOUNT_ID, ACCOUNT_ID_2);

        // when
        moneyTransferService.transfer(ACCOUNT_ID, ACCOUNT_ID_2, new Transfer(EIGHT));

        // then
        Mockito.verify(accountService).getBalance(ACCOUNT_ID, connection);
        Mockito.verify(accountService).updateBalance(eq(ACCOUNT_ID), eq(TWENTY_MINUS_EIGHT), any());
        Mockito.verify(accountService).updateBalance(eq(ACCOUNT_ID_2), eq(EIGHT_PLUS_EIGHT), any());
    }

    @Test(expected = NotEnoughBalanceException.class)
    public void shouldThrowExceptionIfTransferMoreThanAccountHas() {
        when(accountService.getBalance(eq(ACCOUNT_ID), any())).thenReturn(TWENTY);
        when(accountService.getBalance(eq(ACCOUNT_ID_2), any())).thenReturn(EIGHT);
        mockRunInTranasctionAndInLock(ACCOUNT_ID, ACCOUNT_ID_2);

        // given
        moneyTransferService.transfer(ACCOUNT_ID, ACCOUNT_ID_2, new Transfer(TWENTY_PLUS_EIGHT));
    }

    private void mockRunInTranasctionAndInLock(String accountId) {
        doAnswer(invocation -> {
            Runnable argument = invocation.getArgument(1);
            argument.run();
            return null;
        }).when(lockService).runInLock(eq(accountId), any());

        doAnswer(invocation -> {
            StatementRunnable argument = invocation.getArgument(0);
            argument.run(connection, null);
            return null;
        }).when(databaseManager).runInTransaction(any());
    }


    private void mockRunInTranasctionAndInLock(String accountId, String accountId2) {
        doAnswer(invocation -> {
            Runnable argument = invocation.getArgument(2);
            argument.run();
            return null;
        }).when(lockService).runInLock(eq(accountId), eq(accountId2), any());

        doAnswer(invocation -> {
            StatementRunnable argument = invocation.getArgument(0);
            argument.run(connection, null);
            return null;
        }).when(databaseManager).runInTransaction(any());
    }
}
