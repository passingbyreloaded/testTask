package com.revolut.task.service;

import com.revolut.task.dto.MoneyTransferDTO;
import com.revolut.task.exception.AccountNotFoundException;
import com.revolut.task.exception.InvalidAmountException;
import com.revolut.task.model.Account;
import com.revolut.task.repository.AccountRepository;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void transferMoney(MoneyTransferDTO moneyTransferDTO) throws AccountNotFoundException, InvalidAmountException {

        Account accountFrom = accountRepository.getByNumber(moneyTransferDTO.getAccountNumberFrom())
                .orElseThrow(() -> new AccountNotFoundException("No account for number " + moneyTransferDTO.getAccountNumberFrom()));
        Account accountTo = accountRepository.getByNumber(moneyTransferDTO.getAccountNumberTo())
                .orElseThrow(() -> new AccountNotFoundException("No account for number " + moneyTransferDTO.getAccountNumberTo()));

        Object lock1 = accountFrom.getId() < accountTo.getId() ? accountFrom : accountTo;
        Object lock2 = accountFrom.getId() > accountTo.getId() ? accountFrom : accountTo;

        synchronized (lock1) {
            synchronized (lock2) {
                if (accountFrom.getBalance().compareTo(moneyTransferDTO.getAmount()) < 0) {
                    throw new InvalidAmountException();
                }
                accountFrom.setBalance(accountFrom.getBalance().subtract(moneyTransferDTO.getAmount()));
                accountTo.setBalance(accountTo.getBalance().add(moneyTransferDTO.getAmount()));
                System.out.println(accountFrom);
                System.out.println(accountTo);
            }
        }
    }

    @Override
    public BigDecimal getBalance(String number) throws AccountNotFoundException {
        Account account = accountRepository.getByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException("No account for number " + number));
        return account.getBalance();
    }
}
