package com.bank.account.management.controller;

import com.bank.account.management.dto.AccountStatementDto;
import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/{accountNumber}/account-statement")
    @Operation(summary = "get Account Statement with pagination transactions")
    public ResponseEntity<AccountStatementDto> getAccountStatement(@PathVariable String accountNumber, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(bankAccountService.getAccountStatementBy(accountNumber, page, size));
    }

}
