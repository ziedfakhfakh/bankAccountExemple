package com.bank.account.management.dto;

import java.util.List;

public record AccountStatementDto(BankAccountDto bankAccount, List<BankAccountTransactionDto> bankAccountTransactions,
                                  boolean isLast) {
}
