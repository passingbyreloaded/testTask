package com.revolut.task.exception;

public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException() {
        super("Amount exceeds balance");
    }
}
