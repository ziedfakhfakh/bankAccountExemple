package com.bank.account.management.service.impl;

import com.bank.account.management.dto.ClientDto;
import com.bank.account.management.mapper.ClientMapper;
import com.bank.account.management.model.Client;
import com.bank.account.management.repository.ClientRepository;
import com.bank.account.management.service.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Transactional
    @Override
    public ClientDto createClient(ClientDto clientDto) {

        try {
            Client client = this.clientMapper.toClient(clientDto);
            return this.clientMapper.toClientDto(this.clientRepository.save(client));
        } catch (Exception e) {
            log.error("Unexpected error when creating client", e);
            throw new RuntimeException(e);
        }
    }
}
