package com.bank.account.management.service.impl;

import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.exception.BankAccountNotFoundException;
import com.bank.account.management.exception.InvalidInputException;
import com.bank.account.management.mapper.BankAccountMapper;
import com.bank.account.management.model.BankAccountTransaction;
import com.bank.account.management.model.BankAccount;
import com.bank.account.management.model.CurrentAccount;
import com.bank.account.management.model.type.AccountTransactionType;
import com.bank.account.management.repository.BankAccountRepository;
import com.bank.account.management.service.BankAccountOperationsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BankAccountOperationsServiceImpl implements BankAccountOperationsService {

    private final BankAccountRepository bankAccountRepository;

    private final BankAccountMapper bankAccountMapper;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public BankAccountDto deposit(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new InvalidInputException("The amount must be greaten than 0");
        }
        Optional<BankAccount> optionalBankAccount = this.bankAccountRepository.findByAccountNumber(accountNumber);
        return optionalBankAccount.map(bankAccount -> {
            bankAccount.setBalance(bankAccount.getBalance() + amount);
            addTransactionForBankAccount(amount, bankAccount, AccountTransactionType.DEPOSIT);
            return bankAccountMapper.toBankAccountDto(bankAccountRepository.save(bankAccount));

        }).orElseThrow(() -> new BankAccountNotFoundException("Bank account with number: " + accountNumber + " not found"));

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public BankAccountDto withdrawal(String accountNumber, double amount) {
        if (amount >= 0) {
            throw new InvalidInputException("The amount must be less than 0");
        }
        Optional<BankAccount> optionalBankAccount = this.bankAccountRepository.findByAccountNumber(accountNumber);
        return optionalBankAccount.map(bankAccount -> {
            if ((bankAccount instanceof CurrentAccount) && !((CurrentAccount) bankAccount).isHasBalance(amount)) {
                throw new InvalidInputException("There is not enough balance to withdraw the following amount: "  + amount);
            }
            bankAccount.setBalance(bankAccount.getBalance() + amount);
            addTransactionForBankAccount(amount, bankAccount, AccountTransactionType.DEPOSIT);
            return bankAccountMapper.toBankAccountDto(bankAccountRepository.save(bankAccount));

        }).orElseThrow(() -> new BankAccountNotFoundException("Bank account with number: " + accountNumber + " not found"));

    }

    private static void addTransactionForBankAccount(double amount, BankAccount bankAccount, AccountTransactionType type) {
        if (bankAccount.getBankAccountTransactions() == null)
            bankAccount.setBankAccountTransactions(new ArrayList<>());
        bankAccount.getBankAccountTransactions().add(
                BankAccountTransaction.builder()
                        .type(type)
                        .amount(amount)
                        .bankAccount(bankAccount)
                        .build()
        );
    }
}
