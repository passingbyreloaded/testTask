package com.revolut.task.repository;

import com.revolut.task.model.Account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> getByNumber(String number);
}
