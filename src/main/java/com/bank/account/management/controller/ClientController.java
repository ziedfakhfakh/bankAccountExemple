package com.bank.account.management.controller;

import com.bank.account.management.dto.ClientDto;
import com.bank.account.management.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Client Management")
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Operation(summary = "Create new Client")
    public ResponseEntity<ClientDto> createBankAccount(@RequestBody ClientDto clientDto) {
        return new ResponseEntity<>(clientService.createClient(clientDto), HttpStatus.CREATED);
    }

}
