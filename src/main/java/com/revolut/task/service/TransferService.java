package com.revolut.task.service;

import com.revolut.task.dto.MoneyTransferDTO;
import com.revolut.task.exception.AccountNotFoundException;
import com.revolut.task.exception.InvalidAmountException;

public interface TransferService {

    void transferMoney(MoneyTransferDTO moneyTransferDTO) throws AccountNotFoundException, InvalidAmountException;
}
