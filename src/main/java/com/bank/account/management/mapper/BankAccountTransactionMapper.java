package com.bank.account.management.mapper;

import com.bank.account.management.dto.BankAccountTransactionDto;
import com.bank.account.management.model.BankAccountTransaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankAccountTransactionMapper {

    List<BankAccountTransactionDto> toBankAccountTransactionDtos(List<BankAccountTransaction> bankAccountTransactions);
}
