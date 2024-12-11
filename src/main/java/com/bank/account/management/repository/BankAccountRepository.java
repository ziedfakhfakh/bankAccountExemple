package com.bank.account.management.repository;

import com.bank.account.management.model.BankAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepository extends CrudRepository<BankAccount, UUID> {

    Optional<BankAccount> findByAccountNumber(String accountNumber);
}
