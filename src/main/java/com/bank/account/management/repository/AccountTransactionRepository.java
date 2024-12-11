package com.bank.account.management.repository;

import com.bank.account.management.model.BankAccountTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AccountTransactionRepository extends CrudRepository<BankAccountTransaction, UUID> {
}
