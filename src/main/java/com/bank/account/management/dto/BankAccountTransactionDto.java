package com.bank.account.management.dto;

import com.bank.account.management.model.type.AccountTransactionType;

import java.time.LocalDateTime;

public record BankAccountTransactionDto(LocalDateTime creationDate, LocalDateTime updateDate, double amount,
                                        AccountTransactionType type) {
}
