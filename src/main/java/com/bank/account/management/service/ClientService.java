package com.bank.account.management.service;

import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.dto.ClientDto;

public interface ClientService {

    ClientDto createClient(ClientDto client);
}
