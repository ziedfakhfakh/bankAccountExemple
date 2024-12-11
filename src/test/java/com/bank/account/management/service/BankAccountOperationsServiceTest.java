package com.bank.account.management.service;

import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.mapper.BankAccountMapper;
import com.bank.account.management.model.BankAccount;
import com.bank.account.management.model.CurrentAccount;
import com.bank.account.management.model.type.AccountTransactionType;
import com.bank.account.management.model.type.BankAccountType;
import com.bank.account.management.repository.BankAccountRepository;
import com.bank.account.management.service.impl.BankAccountOperationsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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


    @Test
    void testDeposit_withInValidData_withAmountNotValid() {
        //GIVEN
        CurrentAccount bankAccount = CurrentAccount.builder()
                .accountNumber("123456")
                .balance(0.0)
                .overdraft(50.0)
                .build();
        //WHEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> bankAccountOperationsService.deposit("123456", -200));

        //THEN
        assertEquals("The amount must be greaten than 0", exception.getMessage());
    }

    @Test
    void testWithdrawal_withValidData() {
        //GIVEN
        CurrentAccount bankAccount = CurrentAccount.builder()
                .accountNumber("123456")
                .balance(500.0)
                .build();
        BankAccountDto bankAccountDto = new BankAccountDto("123456", BankAccountType.CURRENT_ACCOUNT, 520.0, null);

        Mockito.when(bankAccountRepository.findByAccountNumber("123456"))
                .thenReturn(Optional.of(bankAccount));
        Mockito.when(bankAccountMapper.toBankAccountDto(Mockito.any(BankAccount.class)))
                .thenReturn(bankAccountDto);
        Mockito.when(bankAccountRepository.save(Mockito.any(BankAccount.class)))
                .thenReturn(bankAccount);
        //WHENE
        BankAccountDto result = bankAccountOperationsService.withdrawal("123456", -100.0);
        //THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(520.0, result.balance());
        Mockito.verify(bankAccountRepository).save(bankAccount);
    }


    @Test
    void testWithdrawal_withInValidData_withNoBalanceEnough() {
        //GIVEN
        CurrentAccount bankAccount = CurrentAccount.builder()
                .accountNumber("123456")
                .balance(0.0)
                .overdraft(50.0)
                .build();
        //WHEN
        Mockito.when(bankAccountRepository.findByAccountNumber("123456"))
                .thenReturn(Optional.of(bankAccount));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> bankAccountOperationsService.withdrawal("123456", -200));

        //THEN
        assertEquals("There is not enough balance to withdraw the following amount: -200.0", exception.getMessage());
        verify(bankAccountRepository, times(1)).findByAccountNumber("123456");
    }

    @Test
    void testWithdrawal_withInValidData_withAmountNotValid() {
        //GIVEN
        CurrentAccount bankAccount = CurrentAccount.builder()
                .accountNumber("123456")
                .balance(0.0)
                .overdraft(50.0)
                .build();
        //WHEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> bankAccountOperationsService.withdrawal("123456", 200));

        //THEN
        assertEquals("The amount must be less than 0", exception.getMessage());
    }
}
