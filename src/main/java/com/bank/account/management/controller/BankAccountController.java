package com.bank.account.management.controller;

import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.model.BankAccount;
import com.bank.account.management.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bank-accounts")
@Tag(name = "Bank Account Management")
@AllArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    @Operation(summary = "Create new Bank Acount")
    public ResponseEntity<BankAccountDto> createBankAccount(@RequestBody BankAccountCreationRequest bankAccountCreationRequest) {
        return new ResponseEntity<>(bankAccountService.createBankAccount(bankAccountCreationRequest), HttpStatus.CREATED);
    }

}
