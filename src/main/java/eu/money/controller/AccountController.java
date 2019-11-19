package eu.money.controller;

import eu.money.configuration.JsonReponseTransformer;
import eu.money.model.Account;
import eu.money.service.AccountService;
import eu.money.validation.Validator;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static eu.money.util.JsonMapper.fromJson;
import static spark.Spark.*;

public class AccountController {

    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    public AccountController(final AccountService accountService, final Validator accountValidator) {
        get("/accounts/:accountId", (req, res) ->
                accountService.findByIdOrThrowException(req.params(":accountId")), new JsonReponseTransformer());

        post("/accounts", (req, res) -> {
            logger.info("Received request to create account {}", req.body());
            accountValidator.validate(fromJson(req.body(), Account.class));
            res.status(HttpStatus.CREATED_201);
            return accountService.create(fromJson(req.body(), Account.class));
        }, new JsonReponseTransformer());

        after((request, response) ->
                response.type("application/json"));
    }
}
