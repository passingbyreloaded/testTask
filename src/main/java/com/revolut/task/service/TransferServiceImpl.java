package com.revolut.task.service;

import com.revolut.task.dto.MoneyTransferDTO;

public class TransferServiceImpl implements TransferService {

    private final TransferService transferService;

    public TransferServiceImpl(TransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    public void transferMoney(MoneyTransferDTO moneyTransferDTO) {

    }
}
