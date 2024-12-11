package com.bank.account.management.repository;

import com.bank.account.management.model.BankAccountTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankAccountTransactionRepository extends JpaRepository<BankAccountTransaction, UUID> {

    Slice<BankAccountTransaction> findByBankAccount_accountNumber(String accountNumber, Pageable pageable);
}
