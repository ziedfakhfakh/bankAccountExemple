package com.bank.account.management.exception;

public class InvalidBankAccountTypeException extends RuntimeException {

    public InvalidBankAccountTypeException(String message) {
        super(message);
    }
}
