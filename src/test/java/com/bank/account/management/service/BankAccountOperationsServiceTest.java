package com.bank.account.management.service;

import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.mapper.BankAccountMapper;
import com.bank.account.management.model.BankAccount;
import com.bank.account.management.model.type.BankAccountType;
import com.bank.account.management.repository.BankAccountRepository;
import com.bank.account.management.service.impl.BankAccountOperationsServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bank.account.management.model.type.AccountTransactionType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BankAccountOperationsServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private BankAccountMapper bankAccountMapper;

    private BankAccountOperationsService bankAccountOperationsService;


    @BeforeEach
    void setUp() {
        this.bankAccountOperationsService = new BankAccountOperationsServiceImpl(bankAccountRepository, bankAccountMapper);
    }

    @Test
    void deposit_ShouldIncreaseBalanceAndAddTransaction() {

        // GIVEN
        String accountNumber = "123456789";
        double depositAmount = 500.0;

        BankAccount mockBankAccount = BankAccount.builder()
                .accountNumber("123456789")
                .balance(1000.0)
                .build();

        BankAccountDto bankAccountDto = new BankAccountDto(accountNumber, BankAccountType.CURRENT_ACCOUNT, 1500.0, null);

        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(mockBankAccount));
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(mockBankAccount);
        when(bankAccountMapper.toBankAccountDto(any(BankAccount.class))).thenReturn(bankAccountDto);

        //WHEN
        BankAccountDto result = bankAccountOperationsService.deposit(accountNumber, depositAmount);
        //THEN
        assertNotNull(result);
        assertEquals(accountNumber, result.accountNumber());
        assertEquals(1500.0, result.balance());
        assertEquals(1, mockBankAccount.getBankAccountTransactions().size());

        assertEquals(AccountTransactionType.DEPOSIT, mockBankAccount.getBankAccountTransactions().get(0).getType());
        assertEquals(depositAmount, mockBankAccount.getBankAccountTransactions().get(0).getAmount());
        verify(bankAccountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));

    }

    @Test
    void deposit_ShouldThrowException_WhenAccountNotFound() {
        //GIVEN
        String accountNumber = "987654321";
        double depositAmount = 500.0;
        //WHEN
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> bankAccountOperationsService.deposit(accountNumber, depositAmount));
        //THEN
        assertEquals("Bank account with number: 987654321 not found", exception.getMessage());
        verify(bankAccountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(bankAccountRepository, never()).save(any(BankAccount.class));

    }


}
