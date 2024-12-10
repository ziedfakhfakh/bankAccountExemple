package com.bank.account.management.service;

import com.bank.account.management.dto.ClientDto;
import com.bank.account.management.model.Address;
import com.bank.account.management.model.Client;
import com.bank.account.management.repository.ClientRepository;
import com.bank.account.management.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    private ClientService clientService;

    @BeforeEach
    void setUp() {
        this.clientService = new ClientServiceImpl(clientRepository);
    }

    @Test
    void createClient_ShouldSaveClientAndReturnDto() {

        //GIVEN
        ClientDto clientDto = new ClientDto("customer123", "firstName", "lastName", "email@email.com", new Address());

        Client client = Client.builder()
                .email(clientDto.email())
                .customerId(clientDto.customerId())
                .build();

        when(clientRepository.save(any(Client.class))).thenReturn(client);
        //WHEN
        ClientDto result = clientService.createClient(clientDto);

        //THEN
        assertNotNull(result);
        assertEquals(clientDto.email(), result.email());
        assertEquals(clientDto.customerId(), result.customerId());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void createClient_ShouldThrowRuntimeException_WhenRepositoryFails() {
        //GIVEN
        ClientDto clientDto = new ClientDto("customer123", "firstName", "lastName", "email@email.com", new Address());
        when(clientRepository.save(any(Client.class))).thenThrow(new RuntimeException("Database error during commit"));

        //WHEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.createClient(clientDto));
        //GIVEN
        assertEquals("Database error during commit", exception.getCause().getMessage());
        verify(clientRepository, times(1)).save(any(Client.class));
    }


}