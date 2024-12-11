package com.bank.account.management.mapper;

import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.dto.ClientDto;
import com.bank.account.management.model.BankAccount;
import com.bank.account.management.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDto toClientDto(Client client);

    Client toClient(ClientDto clientDto);
}
