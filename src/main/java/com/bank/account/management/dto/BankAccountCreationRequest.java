package com.bank.account.management.dto;

import com.bank.account.management.model.type.BankAccountType;

public record BankAccountCreationRequest(BankAccountType bankAccountType, String accountNumber, String customerId, double balance,
                                         double overdraft, double savingsInterestRate) {
}
