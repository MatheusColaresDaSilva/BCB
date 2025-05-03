package com.bcb.bcb.service;

import com.bcb.bcb.dto.request.ClientRequestDTO;
import com.bcb.bcb.dto.response.ClientResponseDTO;
import com.bcb.bcb.entity.Client;
import com.bcb.bcb.enums.DocumentEnum;
import com.bcb.bcb.enums.PlanEnum;
import com.bcb.bcb.exception.ClientNotFoundException;
import com.bcb.bcb.exception.InvalidDocumentException;
import com.bcb.bcb.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientResponseDTO> findAll() {
        List<ClientResponseDTO> clientResponse = new ArrayList<>();

        List<Client> client = clientRepository.findAll();
        client.forEach(c -> clientResponse.add(ClientResponseDTO.fromEntity((c))));

        return clientResponse ;

    }

    public ClientResponseDTO findById(Long id) {
        final Client client = getClientById(id);
        return  ClientResponseDTO.fromEntity(client);
    }

    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO clientRequestDTO) {
        var client = Client.fromDTO(clientRequestDTO);
        return ClientResponseDTO.fromEntity(clientRepository.save(client));
    }


    @Transactional
    public ClientResponseDTO updateClient(ClientRequestDTO pessoaRequestDTO, Long id) {
        final Client client = getClientById(id);
        final Client clientUpdated = updateContentClientDtoToEntity(pessoaRequestDTO, client);

        return ClientResponseDTO.fromEntity(clientRepository.save(clientUpdated));
    }

    private Client updateContentClientDtoToEntity(ClientRequestDTO dto, Client client) {

        Optional.ofNullable(dto.getName()).ifPresent(client::setName);

        if (dto.getDocumentId() != null && dto.getDocumentType() != null) {
            DocumentEnum documentType = DocumentEnum.fromCode(dto.getDocumentType());

            if (!documentType.isValid(dto.getDocumentId())) {
                throw new InvalidDocumentException(dto.getDocumentType());
            }

            client.setDocumentId(dto.getDocumentId());
            client.setDocumentType(documentType);
        }

        Optional.ofNullable(dto.getPlanType()).map(PlanEnum::valueOf).ifPresent(client::setPlanType);
        Optional.ofNullable(dto.getBalance()).ifPresent(client::setBalance);
        Optional.ofNullable(dto.getLimit()).ifPresent(client::setLimitBalance);
        Optional.ofNullable(dto.getActive()).ifPresent(client::setActive);

        return client;
    }

    public BigDecimal getBalanceClient(Long id) {
        final Client client = getClientById(id);
        return client.getBalanceValue();
    }

    private Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException());
    }


    public Client getClientByDocument(String documentId) {
        return clientRepository.findByDocumentId(documentId).orElseThrow(() -> new ClientNotFoundException());
    }

}
