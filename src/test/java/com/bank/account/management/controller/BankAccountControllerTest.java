package com.bank.account.management.controller;

import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.model.type.BankAccountType;
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
class BankAccountControllerTest {

    @Mock
    private BankAccountService bankAccountService;

    private BankAccountController bankAccountController;


    @BeforeEach
    void init() {
        this.bankAccountController = new BankAccountController(bankAccountService);
    }

    @Test
    void createBankAccount_ShouldReturnCreatedBankAccount() {

        //GIVEN
        BankAccountCreationRequest request = new BankAccountCreationRequest(

                BankAccountType.CURRENT_ACCOUNT,
                "123456789",
                "customerId",
                1000.0,
                500.0,
                0
        );

        BankAccountDto responseDto = new BankAccountDto("123456789", "customerId", BankAccountType.CURRENT_ACCOUNT, 1000.0);

        //WHEN
        when(bankAccountService.createBankAccount(request)).thenReturn(responseDto);
        ResponseEntity<BankAccountDto> response = bankAccountController.createBankAccount(request);
        //THEN
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(bankAccountService, times(1)).createBankAccount(request);
    }
}

