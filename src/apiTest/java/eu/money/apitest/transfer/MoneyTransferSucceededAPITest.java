package eu.money.apitest.transfer;

import com.jayway.restassured.response.Response;
import eu.money.model.Account;
import eu.money.model.Transfer;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MoneyTransferSucceededAPITest extends MoneyTransferAbstractTest {

    @Test
    public void shouldDeposit() {
        // given
        Account account = createAccount(new Account("test", "owner"));

        // when
        deposit(account.getPublicId(), new Transfer(new BigDecimal(10)));

        // then
        Account depositedAccount = getAccount(account.getPublicId());
        assertThat(depositedAccount.getBalance(), equalTo(new BigDecimal(10)));
    }

    @Test
    public void shouldWithdraw() {
        // given
        Account account = createAccount(new Account("test", "owner"));
        deposit(account.getPublicId(), new Transfer(new BigDecimal(10)));

        // when
        widthdraw(account.getPublicId(), new Transfer(new BigDecimal(5)));

        // then
        Account depositedAccount = getAccount(account.getPublicId());
        assertThat(depositedAccount.getBalance(), equalTo(new BigDecimal(5)));
    }

    @Test
    public void shouldTransfer() {
        // given
        Account sourceAccount = createAccount(new Account("test", "owner"));
        deposit(sourceAccount.getPublicId(), new Transfer(new BigDecimal(10)));

        Account targetAccount = createAccount(new Account("test", "owner"));
        deposit(targetAccount.getPublicId(), new Transfer(new BigDecimal(3)));

        // when
        transfer(sourceAccount.getPublicId(), targetAccount.getPublicId(), new Transfer(new BigDecimal(4)));

        // then
        Account sourceAccountAfterTransfer = getAccount(sourceAccount.getPublicId());
        Account targetAccountAfterTransfer = getAccount(targetAccount.getPublicId());
        assertThat(sourceAccountAfterTransfer.getBalance(), equalTo(new BigDecimal(6)));
        assertThat(targetAccountAfterTransfer.getBalance(), equalTo(new BigDecimal(7)));
    }

    @Test
    public void shouldTransferOnlyOnceAndThrowExceptionsForOthers() throws InterruptedException {
        // given
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);

        Account sourceAccount = createAccountAndDeposit(new Account("test", "owner"), new Transfer(new BigDecimal(10)));
        Account targetAccount = createAccountAndDeposit(new Account("test", "owner"), new Transfer(new BigDecimal(6)));

        Callable<Response> responseCallable = () -> transfer(sourceAccount.getPublicId(), targetAccount.getPublicId(), new Transfer(new BigDecimal(6)));
        List<Callable<Response>> tasks = Arrays.asList(responseCallable, responseCallable, responseCallable);

        // when
        List<Future<Response>> responses = executor.invokeAll(tasks);

        // then
        Account sourceAccountAfterTransfer = getAccount(sourceAccount.getPublicId());
        Account targetAccountAfterTransfer = getAccount(targetAccount.getPublicId());

        assertThat(sourceAccountAfterTransfer.getBalance(), equalTo(new BigDecimal(4)));
        assertThat(targetAccountAfterTransfer.getBalance(), equalTo(new BigDecimal(12)));
        assertThatOnlyOneRequestSucceeded(responses);
    }

    private void assertThatOnlyOneRequestSucceeded(List<Future<Response>> responses) {
        ArrayList<Response> httpsStatus200Responses = responses.stream()
                .map(MoneyTransferSucceededAPITest::getResponse)
                .filter(response -> response.statusCode() == HttpStatus.OK_200)
                .collect(Collectors.toCollection(ArrayList::new));
        Assert.assertEquals(new Integer(httpsStatus200Responses.size()), new Integer(1));
    }

    private Account createAccountAndDeposit(Account account, Transfer transfer) {
        Account sourceAccount = createAccount(account);
        deposit(sourceAccount.getPublicId(), transfer);
        return sourceAccount;
    }

    private static Response getResponse(Future<Response> responseFuture) {
        try {
            return responseFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while processing responses");
        }
    }


}

