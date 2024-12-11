package com.bank.account.management.service;

import com.bank.account.management.dto.AccountStatementDto;
import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;

public interface BankAccountService {

    BankAccountDto createBankAccount(BankAccountCreationRequest bankAccountCreationRequest);

    AccountStatementDto getAccountStatementBy(String accountNumber, int page, int size);

}
