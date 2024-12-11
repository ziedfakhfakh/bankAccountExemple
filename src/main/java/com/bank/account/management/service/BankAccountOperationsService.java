package com.bank.account.management.service;

import com.bank.account.management.dto.BankAccountDto;

public interface BankAccountOperationsService {

    BankAccountDto deposit(String accountNumber, double amount);

    BankAccountDto withdrawal(String accountNumber, double amount);
}
