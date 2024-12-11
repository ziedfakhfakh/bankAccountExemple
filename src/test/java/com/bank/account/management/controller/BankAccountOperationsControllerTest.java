package com.bank.account.management.controller;

import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.model.type.BankAccountType;
import com.bank.account.management.service.BankAccountOperationsService;
import com.bank.account.management.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountOperationsControllerTest {

    @Mock
    private BankAccountOperationsService bankAccountOperationsService;

    private BankAccountOperationsController bankAccountOperationsController;

    @BeforeEach
    void init() {
        this.bankAccountOperationsController = new BankAccountOperationsController(bankAccountOperationsService);
    }

    @Test
    void deposit_ShouldReturnUpdatedBankAccount() {

        //GIVEN
        String accountNumber = "123456789";
        double depositAmount = 500.0;

        BankAccountDto expectedResponse = new BankAccountDto(accountNumber,  BankAccountType.CURRENT_ACCOUNT, 1500.0, null);

        when(bankAccountOperationsService.deposit(accountNumber, depositAmount)).thenReturn(expectedResponse);

        //WHEN
        ResponseEntity<BankAccountDto> response = bankAccountOperationsController.deposit(accountNumber, depositAmount);
        //THEN
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(bankAccountOperationsService, times(1)).deposit(accountNumber, depositAmount);
    }


}

