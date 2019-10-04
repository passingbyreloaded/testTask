package com.revolut.task;

import com.google.gson.JsonSyntaxException;
import com.revolut.task.dto.MoneyTransferDTO;
import com.revolut.task.exception.AccountNotFoundException;
import com.revolut.task.exception.InvalidAmountException;
import com.revolut.task.exception.RequestValidationException;
import com.revolut.task.service.AccountService;
import com.revolut.task.util.ComponentsFactory;
import com.revolut.task.util.JsonHelper;

import java.math.BigDecimal;

import static spark.Spark.*;

public class Application {

    private static final int OK = 200;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_FOUND = 404;
    private static final int NOT_ACCEPTABLE = 406;

    public static void main(String[] args) {

        final AccountService accountService = ComponentsFactory.getAccountService();

        port(8080);

        get("/hello", (req, res) -> "Hello, world");

        get("/balance/:number", (request, response) -> {
            try {
                BigDecimal balance = accountService.getBalance(request.params(":number"));
                response.status(OK);
                return balance;
            } catch (AccountNotFoundException ex) {
                response.status(NOT_FOUND);
                return ex.getMessage();
            }
        });

        post("/transfer", (request, response) -> {
            try {
                MoneyTransferDTO moneyTransferDTO = JsonHelper.fromJson(request.body(), MoneyTransferDTO.class);
                accountService.transferMoney(moneyTransferDTO);
            } catch (JsonSyntaxException | RequestValidationException ex) {
                response.status(BAD_REQUEST);
                return "Invalid request: " + ex.getMessage();
            } catch (AccountNotFoundException ex) {
                response.status(NOT_FOUND);
                return ex.getMessage();
            } catch (InvalidAmountException ex) {
                response.status(NOT_ACCEPTABLE);
                return ex.getMessage();
            }
            response.status(OK);
            return "The amount transferred successfully";
        });
    }
}
