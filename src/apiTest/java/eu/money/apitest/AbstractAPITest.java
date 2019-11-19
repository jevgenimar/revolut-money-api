package eu.money.apitest;

import eu.money.MoneyTransfer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import spark.Spark;

public class AbstractAPITest {

    @BeforeClass
    public static void beforeClass() {
        MoneyTransfer.main(null);
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
        Spark.awaitStop();
    }

}
