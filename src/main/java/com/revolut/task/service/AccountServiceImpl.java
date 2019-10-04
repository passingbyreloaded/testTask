package com.revolut.task.service;

import com.revolut.task.dto.MoneyTransferDTO;
import com.revolut.task.exception.AccountNotFoundException;
import com.revolut.task.exception.InvalidAmountException;
import com.revolut.task.exception.RequestValidationException;
import com.revolut.task.model.Account;
import com.revolut.task.repository.AccountRepository;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void transferMoney(MoneyTransferDTO moneyTransferDTO) {
        validateTransferRequest(moneyTransferDTO);

        Account accountFrom = accountRepository.getByNumber(moneyTransferDTO.getAccountNumberFrom())
                .orElseThrow(() -> new AccountNotFoundException(moneyTransferDTO.getAccountNumberFrom()));
        Account accountTo = accountRepository.getByNumber(moneyTransferDTO.getAccountNumberTo())
                .orElseThrow(() -> new AccountNotFoundException(moneyTransferDTO.getAccountNumberTo()));

        Object lock1 = accountFrom.getId() < accountTo.getId() ? accountFrom : accountTo;
        Object lock2 = accountFrom.getId() > accountTo.getId() ? accountFrom : accountTo;

        synchronized (lock1) {
            synchronized (lock2) {
                if (accountFrom.getBalance().compareTo(moneyTransferDTO.getAmount()) < 0) {
                    throw new InvalidAmountException();
                }
                accountFrom.setBalance(accountFrom.getBalance().subtract(moneyTransferDTO.getAmount()));
                accountTo.setBalance(accountTo.getBalance().add(moneyTransferDTO.getAmount()));
                //todo delete!
                System.out.println(accountFrom);
                System.out.println(accountTo);
            }
        }
    }

    @Override
    public BigDecimal getBalance(String number) {
        return accountRepository.getByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number))
                .getBalance();
    }

    private void validateTransferRequest(MoneyTransferDTO moneyTransferDTO) {
        if (moneyTransferDTO.getAccountNumberFrom() == null ||
                moneyTransferDTO.getAccountNumberTo() == null ||
                moneyTransferDTO.getAmount() == null ||
                moneyTransferDTO.getAccountNumberFrom().isEmpty() ||
                moneyTransferDTO.getAccountNumberTo().isEmpty()) {
            throw new RequestValidationException("Request fields cannot be null or empty");
        } else if (moneyTransferDTO.getAccountNumberFrom().equals(moneyTransferDTO.getAccountNumberTo())) {
            throw new RequestValidationException("Accounts cannot be the same");
        } else if (moneyTransferDTO.getAmount().compareTo(new BigDecimal(0)) <= 0) {
            throw new RequestValidationException("Amount must be positive number");
        }
    }
}
