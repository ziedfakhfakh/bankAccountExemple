package com.bank.account.management.dto;

import com.bank.account.management.model.type.BankAccountType;

public record BankAccountDto(String accountNumber, BankAccountType type, double balance, ClientDto client) {
}
