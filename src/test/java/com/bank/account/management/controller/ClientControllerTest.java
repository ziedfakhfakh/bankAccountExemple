package com.bank.account.management.controller;

import com.bank.account.management.dto.ClientDto;
import com.bank.account.management.service.ClientService;
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
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    private ClientController clientController;


    @BeforeEach
    void init() {
        this.clientController = new ClientController(clientService);
    }

    @Test
    void createClient_ShouldReturnCreatedClient() {

        //GIVEN
        ClientDto clientDto = new ClientDto("customer123", "firstName", "lastName", "email@email.com", null);

        ClientDto responseDto = new ClientDto("customer123", "firstName", "lastName", "email@email.com", null);

        //WHEN
        when(clientService.createClient(clientDto)).thenReturn(responseDto);
        ResponseEntity<ClientDto> response = clientController.createBankAccount(clientDto);
        //THEN
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(clientService, times(1)).createClient(clientDto);
    }
}

