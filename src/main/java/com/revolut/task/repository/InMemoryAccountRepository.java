package com.revolut.task.repository;

import com.revolut.task.model.Account;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {

    private static final Map<String, Account> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> getByNumber(String number) {
        return Optional.ofNullable(storage.get(number));
    }

    @Override
    public void save(Account account) {
        storage.put(account.getNumber(), account);
    }
}
