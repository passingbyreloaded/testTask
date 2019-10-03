package com.revolut.task;

import com.revolut.task.repository.AccountRepository;
import com.revolut.task.repository.InMemoryAccountRepository;
import com.revolut.task.service.AccountService;
import com.revolut.task.service.AccountServiceImpl;

public class ComponentsFactory {

    public static AccountService getAccountService() {
        return new AccountServiceImpl(getAccountRepository());
    }

    public static AccountRepository getAccountRepository() {
        return new InMemoryAccountRepository();
    }
}
