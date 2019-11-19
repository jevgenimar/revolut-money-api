package eu.money.controller;

import eu.money.constant.ErrorCode;
import eu.money.exception.CustomException;
import eu.money.model.Error;
import eu.money.util.JsonMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;

import static spark.Spark.exception;

public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
        exception(Exception.class, (exception, request, response) -> {
            logger.error(exception.getMessage(), exception);
            response.type("application/json");
            if (exception instanceof CustomException) {
                prepareResponse(response, (CustomException) exception);
            } else if (exception.getCause() instanceof CustomException) {
                prepareResponse(response, (CustomException) exception.getCause());
            } else {
                response.body(JsonMapper.toJson(new Error(exception.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR.name())));
                response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            }
        });
    }

    private void prepareResponse(Response response, CustomException exception) {
        response.body(JsonMapper.toJson(new Error(exception.getMessage(), exception.getCode())));
        response.status(exception.getHttpStatus());
    }
}
