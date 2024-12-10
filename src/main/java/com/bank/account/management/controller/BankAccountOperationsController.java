package com.bank.account.management.controller;

import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.model.BankAccount;
import com.bank.account.management.service.BankAccountOperationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bank-account-operations")
@Tag(name = "Bank Account Operation")
@AllArgsConstructor
public class BankAccountOperationsController {

    private final BankAccountOperationsService bankAccountOperationsService;

    @PutMapping("/{accountNumber}/deposit")
    @Operation(summary = "Deposit money")
    public ResponseEntity<BankAccountDto> deposit(@PathVariable String accountNumber, @RequestParam double amount) {
        return null;
    }

}
