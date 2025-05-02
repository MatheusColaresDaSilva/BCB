package com.bcb.bcb.service;

import com.bcb.bcb.dto.request.ClientRequestDTO;
import com.bcb.bcb.dto.response.ClientResponseDTO;
import com.bcb.bcb.entity.Client;
import com.bcb.bcb.exception.ClientNotFoundException;
import com.bcb.bcb.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

//    public List<ClientResponseDTO> findAll(List page) {
//        List<ClientResponseDTO> clientResponse = new ArrayList<>();
//
//        List<Client> client = clientRepository.findAll(page);
//        client.forEach(pessoa -> clientResponse.add(clientEntityToDto(pessoa)));
//
//        return  new PageImpl<>(clientResponse, page, client.getTotalElements()) ;
//
//    }
//
    public ClientResponseDTO findById(Long id) {
        final Client client = getClientById(id);
        return  ClientResponseDTO.fromEntity(client);
    }

    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO clientRequestDTO) {
        var client = Client.fromDTO(clientRequestDTO);
        return ClientResponseDTO.fromEntity(clientRepository.save(client));
    }


//    @Transactional
//    public ClientResponseDTO updateClient(ClientRequestDTO pessoaRequestDTO, Long id) {
//        final Client client = getClientById(id);
//        final Client clientUpdated = updateContentClientDtoToEntity(pessoaRequestDTO, client);
//
//        return clientEntityToDto(clientRepository.save(clientUpdated));
//    }
//
//    @Transactional
//    public void deleteClient(Long id) {
//        final Client pessoa = getClientById(id);
//        clientRepository.delete(pessoa);
//    }
//
//    private Client updateContentClientDtoToEntity(ClientRequestDTO clientRequestDTO, Client client) {
//
//        Optional.ofNullable(clientRequestDTO.getName()).ifPresent(client::setName);
//        Optional.ofNullable(clientRequestDTO.getCpf()).ifPresent(client::setCpf);
//        Optional.ofNullable(clientRequestDTO.getCompanyName()).ifPresent(client::setCompanyName);
//        Optional.ofNullable(clientRequestDTO.getCnpj()).ifPresent(client::setCnpj);
//        Optional.ofNullable(clientRequestDTO.getPhoneNumber()).ifPresent(client::setPhoneNumber);
//
//        return client;
//    }
//
    private Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException());
    }


    public Client getClientByDocument(String documentId) {
        return clientRepository.findByDocumentId(documentId).orElseThrow(() -> new ClientNotFoundException());
    }

}
