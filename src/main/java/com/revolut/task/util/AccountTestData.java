package com.revolut.task.util;

import com.revolut.task.model.Account;
import com.revolut.task.repository.AccountRepository;

import java.math.BigDecimal;

public class AccountTestData {

    public static final Account ACCOUNT1 = new Account(1L,
            "44445555444455554444555544445555",
            new BigDecimal("0"));
    public static final Account ACCOUNT2 = new Account(2L,
            "55556666555566665555666655556666",
            new BigDecimal("100"));
    public static final Account ACCOUNT3 = new Account(3L,
            "66667777666677776666777766667777",
            new BigDecimal("5000.55"));
    public static final Account ACCOUNT4 = new Account(4L,
            "77778888777788887777888877778888",
            new BigDecimal("1000"));
    public static final Account NOT_EXISTING_ACCOUNT = new Account(5L,
            "11111111111111111111111111111111",
            new BigDecimal("100"));


    public static void populateStorage(AccountRepository accountRepository){
        accountRepository.save(ACCOUNT1);
        accountRepository.save(ACCOUNT2);
        accountRepository.save(ACCOUNT3);
        accountRepository.save(ACCOUNT4);
    }
}
