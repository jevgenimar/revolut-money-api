package eu.money.apitest.transfer;

import com.jayway.restassured.response.Response;
import eu.money.model.Account;
import eu.money.model.Transfer;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MoneyTransferFailedAPITest extends MoneyTransferAbstractTest {

    @Test
    public void shouldReturnErrorIfWithdrawBiggerThanBalance() {
        // given
        Account account = createAccount(new Account("test", "owner"));
        deposit(account.getPublicId(), new Transfer(new BigDecimal(10)));

        // when
        Response depositResponse = widthdraw(account.getPublicId(), new Transfer(new BigDecimal(40)));

        // then
        assertThat(depositResponse.statusCode(), equalTo(HttpStatus.BAD_REQUEST_400));
    }

    @Test
    public void shouldReturnErrorForNullDeposit() {
        // given
        Account account = createAccount(new Account("test", "owner"));

        // when
        Response depositResponse = deposit(account.getPublicId(), new Transfer(null));

        // then
        assertThat(depositResponse.statusCode(), equalTo(HttpStatus.BAD_REQUEST_400));
    }

    @Test
    public void shouldReturnErrorForNullWithdraw() {
        // given
        Account account = createAccount(new Account("test", "owner"));

        // when
        Response depositResponse = widthdraw(account.getPublicId(), new Transfer(null));

        // then
        assertThat(depositResponse.statusCode(), equalTo(HttpStatus.BAD_REQUEST_400));
    }

}
