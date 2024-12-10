package com.bank.account.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(InvalidBankAccountTypeException.class)
    public ResponseEntity<String> handleInvalidBankAccountTypeException(InvalidBankAccountTypeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}