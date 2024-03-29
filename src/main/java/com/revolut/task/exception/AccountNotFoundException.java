package com.revolut.task.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {
        super("No account for number " + message);
    }
}
