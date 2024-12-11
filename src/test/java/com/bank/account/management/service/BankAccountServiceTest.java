package com.bank.account.management.service;

import com.bank.account.management.dto.AccountStatementDto;
import com.bank.account.management.dto.BankAccountCreationRequest;
import com.bank.account.management.dto.BankAccountDto;
import com.bank.account.management.exception.InvalidInputException;
import com.bank.account.management.mapper.BankAccountMapper;
import com.bank.account.management.mapper.BankAccountTransactionMapper;
import com.bank.account.management.model.BankAccount;
import com.bank.account.management.model.BankAccountTransaction;
import com.bank.account.management.model.Client;
import com.bank.account.management.model.CurrentAccount;
import com.bank.account.management.model.type.BankAccountStatus;
import com.bank.account.management.model.type.BankAccountType;
import com.bank.account.management.repository.BankAccountRepository;
import com.bank.account.management.repository.BankAccountTransactionRepository;
import com.bank.account.management.repository.ClientRepository;
import com.bank.account.management.service.impl.BankAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.SliceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BankAccountTransactionRepository bankAccountTransactionRepository;

    @Mock
    private BankAccountTransactionMapper bankAccountTransactionMapper;


    private BankAccountService bankAccountService;

    private BankAccountCreationRequest validRequest;
    private BankAccountCreationRequest invalidRequest;

    @BeforeEach
    void setUp() {
        this.bankAccountService = new BankAccountServiceImpl(bankAccountRepository, bankAccountTransactionRepository, clientRepository, bankAccountMapper, bankAccountTransactionMapper);

        validRequest = new BankAccountCreationRequest(BankAccountType.CURRENT_ACCOUNT, "accountNumber", "customerId", 1000.0, 500.0, 0);
        invalidRequest = new BankAccountCreationRequest(null, "accountNumber", "customerId", 1000.0, 500.0, 0);
    }

    @Test
    void testCreateBankAccountValidRequest() {
        //GIVEN
        Client client = Client.builder().customerId("accountNumber").build();
        BankAccount mockBankAccount = CurrentAccount.builder()
                .accountNumber("accountNumber")
                .client(client)
                .status(BankAccountStatus.ACTIVE)
                .balance(1000.0)
                .overdraft(500.0).build();

        BankAccountDto bankAccountDto = new BankAccountDto("accountNumber",  BankAccountType.CURRENT_ACCOUNT, 1000.0, null);
        //WHEN
        when(bankAccountRepository.save(any())).thenReturn(mockBankAccount);
        when(clientRepository.findClientByCustomerId(anyString())).thenReturn(Optional.of(client));
        when(bankAccountMapper.toBankAccountDto(any())).thenReturn(bankAccountDto);

        BankAccountDto result = bankAccountService.createBankAccount(validRequest);
        //THEN
        assertNotNull(result);
        assertEquals("accountNumber", result.accountNumber());
        assertEquals(BankAccountType.CURRENT_ACCOUNT, result.type());
        assertEquals(1000.0, result.balance());
        verify(bankAccountRepository, times(1)).save(any());
    }

    @Test
    void testCreateBankAccountWithInvalidType() {
        InvalidInputException thrown = assertThrows(InvalidInputException.class, () -> {
            bankAccountService.createBankAccount(invalidRequest);
        });
        assertEquals("Type of Bank Account is Null", thrown.getMessage());
    }

    @Test
    void testCreateBankAccountWithExceptionDuringSave() {
        //GIVEN
        when(bankAccountRepository.save(any(BankAccount.class))).thenThrow(new RuntimeException("Database error"));
        when(clientRepository.findClientByCustomerId(anyString())).thenReturn(Optional.of(new Client()));

        //WHEN
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            bankAccountService.createBankAccount(validRequest);
        });
        //THEN
        assertEquals("java.lang.RuntimeException: Database error", thrown.getMessage());
    }

    @Test
    void getAccountStatementBy_ShouldReturnAccountStatement_WhenBankAccountExists() {
        //GIVEN
        BankAccount bankAccount = new BankAccount();
        BankAccountTransaction transaction1 = new BankAccountTransaction();
        BankAccountTransaction transaction2 = new BankAccountTransaction();

        Slice<BankAccountTransaction> transactions = new SliceImpl<>(Arrays.asList(transaction1, transaction2), PageRequest.of(0, 5), true);

        when(bankAccountRepository.findByAccountNumber("accountNumber")).thenReturn(Optional.of(bankAccount));
        when(bankAccountTransactionRepository.findByBankAccount_accountNumber("accountNumber", PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "creationDate"))))
                .thenReturn(transactions);
        when(bankAccountMapper.toBankAccountDto(bankAccount)).thenReturn(
                new BankAccountDto("accountNumber",  BankAccountType.CURRENT_ACCOUNT, 1000.0, null)
        );
        when(bankAccountTransactionMapper.toBankAccountTransactionDtos(transactions.getContent())).thenReturn(null); // Mocked DTO
        //WHEN
        AccountStatementDto accountStatementDto = this.bankAccountService.getAccountStatementBy("accountNumber", 0, 5);
        //THEN
        assertFalse(accountStatementDto.isLast());
        assertEquals(accountStatementDto.bankAccount().accountNumber(), "accountNumber");
    }

}
