package com.bank.account.management.dto;

import com.bank.account.management.model.type.BankAccountType;

public record BankAccountDto(String accountNumber, String customerId, BankAccountType type, double balance) {
}
