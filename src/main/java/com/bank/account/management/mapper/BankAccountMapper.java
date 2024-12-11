package com.bank.account.management.mapper;

import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.model.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    @Mapping(source = "bankAccountType", target ="type" )
    BankAccountDto toBankAccountDto(BankAccount bankAccount);
}
