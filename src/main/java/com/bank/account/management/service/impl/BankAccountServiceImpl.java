package com.bank.account.management.service.impl;

import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.exception.ClientNotFoundException;
import com.bank.account.management.exception.InvalidBankAccountTypeException;
import com.bank.account.management.mapper.BankAccountMapper;
import com.bank.account.management.model.BankAccount;
import com.bank.account.management.model.Client;
import com.bank.account.management.model.CurrentAccount;
import com.bank.account.management.model.SavingsAccount;
import com.bank.account.management.model.type.BankAccountStatus;
import com.bank.account.management.repository.BankAccountRepository;
import com.bank.account.management.repository.ClientRepository;
import com.bank.account.management.service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final ClientRepository clientRepository;

    private final BankAccountMapper bankAccountMapper;

    @Transactional
    @Override
    public BankAccountDto createBankAccount(BankAccountCreationRequest request) {
        if (request.bankAccountType() == null) {
            throw new InvalidBankAccountTypeException("Type of Bank Account is Null: ");
        }
        Client client = this.clientRepository.findClientByCustomerId(request.customerId()).orElseThrow(
                () -> new ClientNotFoundException("Client with customerId: " + request.customerId() + " Not Found"));
        try {
            BankAccount bankAccount =
                    switch (request.bankAccountType()) {
                        case CURRENT_ACCOUNT -> CurrentAccount.builder()
                                .accountNumber(request.accountNumber())
                                .status(BankAccountStatus.ACTIVE)
                                .client(client)
                                .balance(request.balance())
                                .overdraft(request.overdraft()).build();
                        case SAVINGS_ACCOUNT -> SavingsAccount.builder()
                                .accountNumber(request.accountNumber())
                                .status(BankAccountStatus.ACTIVE)
                                .client(client)
                                .balance(request.balance())
                                .savingsInterestRate(request.savingsInterestRate())
                                .build();
                        default ->
                                throw new InvalidBankAccountTypeException("Type of Bank Account is invalid: " + request.bankAccountType());
                    };
            return this.bankAccountMapper.toBankAccountDto(this.bankAccountRepository.save(bankAccount));
        } catch (Exception e) {
            log.error("Unexpected error when creating bank account", e);
            throw new RuntimeException(e);
        }
    }
}
