package com.bank.account.management.dto;

import com.bank.account.management.model.Address;

public record ClientDto(String customerId, String firstName, String lastName, String email, Address address) {
}
