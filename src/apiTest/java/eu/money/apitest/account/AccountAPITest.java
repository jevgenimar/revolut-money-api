package eu.money.apitest.account;

import eu.money.apitest.AbstractAPITest;
import com.jayway.restassured.http.ContentType;
import eu.money.constant.ErrorCode;
import eu.money.model.Account;
import eu.money.util.JsonMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class AccountAPITest extends AbstractAPITest {

    @Test
    public void shouldCreateAccount() {
        Account account = new Account("test", "owner");

        given()
                .accept(ContentType.JSON)
                .body(JsonMapper.toJson(account))
                .contentType(ContentType.JSON)
        .when()
                .post("/accounts")
        .then()
                .statusCode(201)
                .assertThat()
                .body(
                        "iban", equalTo("test"),
                        "owner", equalTo("owner"),
                        "balance", equalTo(0)
                );
    }

    @Test
    public void shouldThrowExceptionOfOwnerIsTooLongs() {
        Account account = new Account("test", RandomStringUtils.random(201));

        given()
                .accept(ContentType.JSON)
                .body(JsonMapper.toJson(account))
                .contentType(ContentType.JSON)
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .assertThat()
                .body(
                        "code", equalTo(ErrorCode.REQUEST_VALIDATION_FAILED.name())
                );
    }

}
