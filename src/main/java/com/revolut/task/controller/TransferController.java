package com.revolut.task.controller;

import com.google.gson.JsonSyntaxException;
import com.revolut.task.dto.MoneyTransferDTO;
import com.revolut.task.exception.AccountNotFoundException;
import com.revolut.task.exception.InvalidAmountException;
import com.revolut.task.exception.RequestValidationException;
import com.revolut.task.service.AccountService;
import com.revolut.task.util.JsonHelper;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.revolut.task.Application.*;

public class TransferController implements Route {

    private final AccountService accountService;

    public TransferController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Object handle(Request request, Response response) {
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
    }
}
