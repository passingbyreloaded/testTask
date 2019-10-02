package com.revolut.task.repository;

import com.revolut.task.model.Account;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {

    private final Map<Long, Account> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> getByNumber(Long number){
        return Optional.ofNullable(storage.get(number));
    }
}
