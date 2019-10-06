package com.revolut.task;

import com.revolut.task.controller.BalanceController;
import com.revolut.task.controller.TransferController;
import com.revolut.task.repository.AccountRepository;
import com.revolut.task.repository.InMemoryAccountRepository;
import com.revolut.task.service.AccountService;
import com.revolut.task.service.AccountServiceImpl;
import com.revolut.task.util.AccountTestData;

import static spark.Spark.*;

public class Application {

    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;
    public static final int NOT_ACCEPTABLE = 406;

    public static void main(String[] args) {

        AccountRepository accountRepository = new InMemoryAccountRepository();
        AccountTestData.populateStorage(accountRepository);
        AccountService accountService = new AccountServiceImpl(accountRepository);

        get("/balance/:number", new BalanceController(accountService));
        post("/transfer", new TransferController(accountService));
    }
}
