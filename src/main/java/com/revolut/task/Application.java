package com.revolut.task;

import com.google.gson.JsonSyntaxException;
import com.revolut.task.dto.MoneyTransferDTO;
import com.revolut.task.exception.AccountNotFoundException;
import com.revolut.task.exception.InvalidAmountException;
import com.revolut.task.service.AccountService;
import com.revolut.task.util.JsonHelper;

import java.math.BigDecimal;

import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {

        final AccountService accountService = ComponentsFactory.getAccountService();

        port(8080);

        get("/hello", (req, res) -> "Hello, world");

        get("/balance/:number", (request, response) -> {
            try {
                BigDecimal balance = accountService.getBalance(request.params(":number"));
                response.status(200);
                return balance;
            } catch (AccountNotFoundException ex) {
                response.status(404);
                return "Account not found: " + ex.getMessage();
            }
        });

        post("/transfer", (request, response) -> {
            try {
                MoneyTransferDTO moneyTransferDTO = JsonHelper.fromJson(request.body(), MoneyTransferDTO.class);

                if (moneyTransferDTO.getAccountNumberFrom() == null ||
                        moneyTransferDTO.getAccountNumberTo() == null ||
                        moneyTransferDTO.getAmount() == null ||
                        moneyTransferDTO.getAccountNumberFrom().isEmpty() ||
                        moneyTransferDTO.getAccountNumberTo().isEmpty()) {
                    response.status(400);
                    return "Request fields cannot be null or empty";
                } else if (moneyTransferDTO.getAccountNumberFrom().equals(moneyTransferDTO.getAccountNumberTo())) {
                    response.status(400);
                    return "Accounts cannot be the same";
                } else if (moneyTransferDTO.getAmount().compareTo(new BigDecimal(0)) <= 0) {
                    response.status(400);
                    return "Amount must be positive number";
                }
                accountService.transferMoney(moneyTransferDTO);
            } catch (JsonSyntaxException ex) {
                response.status(400);
                return "Invalid request: " + ex.getMessage();
            } catch (AccountNotFoundException ex) {
                response.status(404);
                return "Account not found: " + ex.getMessage();
            } catch (InvalidAmountException ex) {
                response.status(406);
                return "Amount exceeds balance";
            }
            response.status(200);
            return "The amount transferred successfully";
        });

    }
}
