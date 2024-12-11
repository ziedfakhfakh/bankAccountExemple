package com.bank.account.management.repository;

import com.bank.account.management.model.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends CrudRepository<Client, UUID> {

    Optional<Client> findClientByCustomerId(String customerId);
}
