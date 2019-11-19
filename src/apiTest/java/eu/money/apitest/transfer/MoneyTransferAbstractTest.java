package eu.money.apitest.transfer;

import eu.money.apitest.AbstractAPITest;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import eu.money.model.Account;
import eu.money.model.Transfer;
import eu.money.util.JsonMapper;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MoneyTransferAbstractTest extends AbstractAPITest {
    Response deposit(String accountId, Transfer transfer) {
        return given()
                .accept(ContentType.JSON)
                .body(JsonMapper.toJson(transfer))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts/deposit/{sourceAccountId}", accountId)
                .andReturn();
    }

    Response widthdraw(String accountId, Transfer transfer) {
        return given()
                .accept(ContentType.JSON)
                .body(JsonMapper.toJson(transfer))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts/withdraw/{sourceAccountId}", accountId)
                .andReturn();
    }

    Response transfer(String sourceAccountId, String targetAccountId, Transfer transfer) {
        return given()
                .accept(ContentType.JSON)
                .body(JsonMapper.toJson(transfer))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts/transfer/{sourceAccountId}/{targetAccountID}", sourceAccountId, targetAccountId)
                .andReturn();
    }

    Account createAccount(Account account) {
        Response response = given()
                .accept(ContentType.JSON)
                .body(JsonMapper.toJson(account))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts");
        assertThat(response.getStatusCode(), equalTo(201));
        return JsonMapper.fromJson(response.body().print(), Account.class);
    }

    protected Account getAccount(String accoutId) {
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("/accounts/{accountID}", accoutId);
        assertThat(response.getStatusCode(), equalTo(200));
        return JsonMapper.fromJson(response.body().print(), Account.class);
    }
}
