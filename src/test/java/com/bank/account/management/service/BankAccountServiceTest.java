package com.bank.account.management.service;

import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.exception.InvalidBankAccountTypeException;
import com.bank.account.management.model.BankAccount;
import com.bank.account.management.model.CurrentAccount;
import com.bank.account.management.model.type.BankAccountStatus;
import com.bank.account.management.model.type.BankAccountType;
import com.bank.account.management.repository.BankAccountRepository;
import com.bank.account.management.service.impl.BankAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    private BankAccountService bankAccountService;

    private BankAccountCreationRequest validRequest;
    private BankAccountCreationRequest invalidRequest;

    @BeforeEach
    void setUp() {
        this.bankAccountService = new BankAccountServiceImpl(bankAccountRepository);

        validRequest = new BankAccountCreationRequest(BankAccountType.CURRENT_ACCOUNT, "accountNumber", "customerId", 1000.0, 500.0, 0);
        invalidRequest = new BankAccountCreationRequest(null, "accountNumber", "customerId", 1000.0, 500.0, 0);
    }

    @Test
    void testCreateBankAccountValidRequest() {
        // Arrange
        BankAccount mockBankAccount = CurrentAccount.builder()
                .accountNumber("accountNumber")
                .status(BankAccountStatus.ACTIVE)
                .balance(1000.0)
                .overdraft(500.0).build();

        when(bankAccountRepository.save(any())).thenReturn(mockBankAccount);

        BankAccountDto result = bankAccountService.createBankAccount(validRequest);

        assertNotNull(result);
        assertEquals("accountNumber", result.accountNumber());
        assertEquals(BankAccountType.CURRENT_ACCOUNT, result.type());
        assertEquals(1000.0, result.balance());
        verify(bankAccountRepository, times(1)).save(any());
    }

    @Test
    void testCreateBankAccountWithInvalidType() {
        // Act & Assert
        InvalidBankAccountTypeException thrown = assertThrows(InvalidBankAccountTypeException.class, () -> {
            bankAccountService.createBankAccount(invalidRequest);
        });

        assertEquals("Type of Bank Account is Null: ", thrown.getMessage());
    }

    @Test
    void testCreateBankAccountWithExceptionDuringSave() {
        // Arrange
        when(bankAccountRepository.save(any(BankAccount.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            bankAccountService.createBankAccount(validRequest);
        });

        assertEquals("java.lang.RuntimeException: Database error", thrown.getMessage());
    }

}
