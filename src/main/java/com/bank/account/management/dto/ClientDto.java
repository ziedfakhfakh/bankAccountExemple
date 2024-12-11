package com.bank.account.management.dto;

public record ClientDto(String customerId, String firstName, String lastName, String email, AddressDto address) {
}
