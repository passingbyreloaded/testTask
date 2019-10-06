package com.revolut.task.service;

import com.revolut.task.dto.MoneyTransferDTO;

import java.math.BigDecimal;

public interface AccountService {

    void transferMoney(MoneyTransferDTO moneyTransferDTO);

    BigDecimal getBalance(String number);
}
