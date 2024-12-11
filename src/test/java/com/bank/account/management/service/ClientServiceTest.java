package com.bank.account.management.service;

import com.bank.account.management.dto.ClientDto;
import com.bank.account.management.mapper.ClientMapper;
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

    @Mock
    private ClientMapper clientMapper;

    private ClientService clientService;

    @BeforeEach
    void setUp() {
        this.clientService = new ClientServiceImpl(clientRepository, clientMapper);
    }

    @Test
    void createClient_ShouldSaveClientAndReturnDto() {

        //GIVEN
        ClientDto clientDto = new ClientDto("customer123", "firstName", "lastName", "email@email.com", null);

        Client client = Client.builder()
                .email(clientDto.email())
                .customerId(clientDto.customerId())
                .build();

        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientMapper.toClientDto(any(Client.class))).thenReturn(clientDto);
        when(clientMapper.toClient(any(ClientDto.class))).thenReturn(client);
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
        ClientDto clientDto = new ClientDto("customer123", "firstName", "lastName", "email@email.com", null);
        when(clientRepository.save(any(Client.class))).thenThrow(new RuntimeException("Database error during commit"));
        when(clientMapper.toClient(any(ClientDto.class))).thenReturn(new Client());
        //WHEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.createClient(clientDto));
        //THEN
        assertEquals("Database error during commit", exception.getCause().getMessage());
        verify(clientRepository, times(1)).save(any(Client.class));
    }


}
