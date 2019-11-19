package eu.money.controller;

import eu.money.configuration.JsonReponseTransformer;
import eu.money.model.Transfer;
import eu.money.service.MoneyTransferService;
import eu.money.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static eu.money.util.JsonMapper.fromJson;
import static spark.Spark.after;
import static spark.Spark.post;

public class MoneyTransferController {

    private static Logger logger = LoggerFactory.getLogger(MoneyTransferController.class);
    private static final String EMPTY_RESPONSE = "";

    public MoneyTransferController(final MoneyTransferService transferService, final Validator transferValidator) {
        post("/accounts/transfer/:sourceAccountId/:targetAccountID", (req, res) -> {
            logger.info("Request to transfer from {} to {}. Payload {}", req.params(":sourceAccountId"), req.params(":targetAccountID"), req.body());
            transferValidator.validate(fromJson(req.body(), Transfer.class));
            transferService.transfer(req.params(":sourceAccountId"), req.params(":targetAccountID"), fromJson(req.body(), Transfer.class));
            return EMPTY_RESPONSE;
        }, new JsonReponseTransformer());

        post("/accounts/deposit/:targetAccountID", (req, res) -> {
            logger.info("Request to deposit deposit to {}. Payload {}", req.params(":targetAccountID"), req.body());
            transferValidator.validate(fromJson(req.body(), Transfer.class));
            transferService.deposit(req.params(":targetAccountID"), fromJson(req.body(), Transfer.class));
            return EMPTY_RESPONSE;
        }, new JsonReponseTransformer());

        post("/accounts/withdraw/:targetAccountID", (req, res) -> {
            logger.info("Request to withdraw from account {}. Payload {}", req.params(":targetAccountID"), req.body());
            transferValidator.validate(fromJson(req.body(), Transfer.class));
            transferService.withdraw(req.params(":targetAccountID"), fromJson(req.body(), Transfer.class));
            return EMPTY_RESPONSE;
        }, new JsonReponseTransformer());

        after((request, response) -> response.type("application/json"));
    }
}
