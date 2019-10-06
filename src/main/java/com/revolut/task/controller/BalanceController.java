package com.revolut.task.controller;

import com.revolut.task.exception.AccountNotFoundException;
import com.revolut.task.service.AccountService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.math.BigDecimal;

import static com.revolut.task.Application.*;

public class BalanceController implements Route {

    private final AccountService accountService;

    public BalanceController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            BigDecimal balance = accountService.getBalance(request.params(":number"));
            response.status(OK);
            return balance;
        } catch (AccountNotFoundException ex) {
            response.status(NOT_FOUND);
            return ex.getMessage();
        }
    }
}
