package com.bank.account.management.service.impl;

import com.bank.account.management.dto.AccountStatementDto;
import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.exception.BankAccountNotFoundException;
import com.bank.account.management.exception.ClientNotFoundException;
import com.bank.account.management.exception.InvalidInputException;
import com.bank.account.management.mapper.BankAccountMapper;
import com.bank.account.management.mapper.BankAccountTransactionMapper;
import com.bank.account.management.model.*;
import com.bank.account.management.model.type.BankAccountStatus;
import com.bank.account.management.repository.BankAccountRepository;
import com.bank.account.management.repository.BankAccountTransactionRepository;
import com.bank.account.management.repository.ClientRepository;
import com.bank.account.management.service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final BankAccountTransactionRepository bankAccountTransactionRepository;

    private final ClientRepository clientRepository;

    private final BankAccountMapper bankAccountMapper;

    private final BankAccountTransactionMapper bankAccountTransactionMapper;

    @Transactional
    @Override
    public BankAccountDto createBankAccount(BankAccountCreationRequest request) {
        if (request.bankAccountType() == null) {
            throw new InvalidInputException("Type of Bank Account is Null");
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
                                throw new InvalidInputException("Type of Bank Account is invalid: " + request.bankAccountType());
                    };
            return this.bankAccountMapper.toBankAccountDto(this.bankAccountRepository.save(bankAccount));
        } catch (Exception e) {
            log.error("Unexpected error when creating bank account", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public AccountStatementDto getAccountStatementBy(String accountNumber, int page, int size) {
        Optional<BankAccount> optionalBankAccount = this.bankAccountRepository.findByAccountNumber(accountNumber);
        return optionalBankAccount.map(bankAccount -> {
            Slice<BankAccountTransaction> bankAccountTransactions = this.bankAccountTransactionRepository.findByBankAccount_accountNumber(accountNumber, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "creationDate")));

            return new AccountStatementDto(
                    this.bankAccountMapper.toBankAccountDto(bankAccount),
                    this.bankAccountTransactionMapper.toBankAccountTransactionDtos(bankAccountTransactions.getContent()),
                    bankAccountTransactions.isLast());

        }).orElseThrow(() -> new BankAccountNotFoundException("Bank account with number: " + accountNumber + " not found"));
    }
}
