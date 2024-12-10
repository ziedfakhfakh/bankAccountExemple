package com.bank.account.management.service;

import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.model.BankAccount;

public interface BankAccountService {

    BankAccountDto createBankAccount(BankAccountCreationRequest bankAccountCreationRequest);
}
