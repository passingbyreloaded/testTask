package com.revolut.task.repository;

import com.revolut.task.model.Account;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {

    private final static Map<String, Account> storage = new ConcurrentHashMap<>();

    static {
        Account account1 = new Account(1l, "44445555", new BigDecimal(0));
        Account account2 = new Account(2l, "55556666", new BigDecimal(100));
        Account account3 = new Account(3l, "66667777", new BigDecimal(50000));
        Account account4 = new Account(4l, "77778888", new BigDecimal(1000));
        storage.put(account1.getNumber(), account1);
        storage.put(account2.getNumber(), account2);
        storage.put(account3.getNumber(), account3);
        storage.put(account4.getNumber(), account4);
    }

    @Override
    public Optional<Account> getByNumber(String number){
        return Optional.ofNullable(storage.get(number));
    }
}
