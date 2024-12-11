package com.bank.account.management.service;

import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.model.BankAccount;

public interface BankAccountOperationsService {

    BankAccountDto deposit(String accountNumber, double amount);

    BankAccountDto withdrawal(String accountNumber, double amount);
}
