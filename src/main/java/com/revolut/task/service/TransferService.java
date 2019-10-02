package com.revolut.task.service;

import com.revolut.task.dto.MoneyTransferDTO;

public interface TransferService {

    void transferMoney(MoneyTransferDTO moneyTransferDTO);
}
