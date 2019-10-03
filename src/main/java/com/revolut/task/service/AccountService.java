package com.revolut.task.service;

import com.revolut.task.dto.MoneyTransferDTO;
import com.revolut.task.exception.AccountNotFoundException;
import com.revolut.task.exception.InvalidAmountException;

import java.math.BigDecimal;

public interface AccountService {

    void transferMoney(MoneyTransferDTO moneyTransferDTO) throws AccountNotFoundException, InvalidAmountException;

    BigDecimal getBalance(String number) throws AccountNotFoundException;
}
