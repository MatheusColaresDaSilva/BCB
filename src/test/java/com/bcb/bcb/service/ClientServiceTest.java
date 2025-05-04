package com.bcb.bcb.service;

import com.bcb.bcb.dto.request.ClientRequestDTO;
import com.bcb.bcb.dto.response.ClientResponseDTO;
import com.bcb.bcb.entity.Client;
import com.bcb.bcb.enums.DocumentEnum;
import com.bcb.bcb.enums.PlanEnum;
import com.bcb.bcb.exception.ClientNotFoundException;
import com.bcb.bcb.exception.InvalidDocumentException;
import com.bcb.bcb.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldReturnAllClients() {
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Matheus");
        client1.setDocumentType(DocumentEnum.CPF);
        client1.setPlanType(PlanEnum.PREPAID);

        Client client2 = new Client();
        client2.setId(1L);
        client2.setName("Fulano");
        client2.setDocumentType(DocumentEnum.CPF);
        client2.setPlanType(PlanEnum.PREPAID);

        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));

        List<ClientResponseDTO> result = clientService.findAll();

        assertEquals(2, result.size());
        assertEquals("Matheus", result.get(0).getName());
        assertEquals("Fulano", result.get(1).getName());
    }

    @Test
    void shouldReturnClientById() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Matheus");
        client.setDocumentType(DocumentEnum.CPF);
        client.setPlanType(PlanEnum.PREPAID);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientResponseDTO result = clientService.findById(1L);

        assertEquals("Matheus", result.getName());
    }

    @Test
    void shouldUpdateClient() {
        Client existing = new Client();
        existing.setId(1L);
        existing.setName("Old Name");
        existing.setDocumentId("123456789");
        existing.setDocumentType(DocumentEnum.CPF);
        existing.setPlanType(PlanEnum.PREPAID);

        ClientRequestDTO request = new ClientRequestDTO();
        request.setName("Updated Name");
        request.setPlanType("PREPAID");
        request.setDocumentId("12345678900");
        request.setDocumentType("CPF");
        request.setBalance(new BigDecimal("100.00"));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));

        ClientResponseDTO result = clientService.updateClient(request, 1L);

        assertEquals("Updated Name", result.getName());
    }

    @Test
    void shouldReturnBalance() {
        Client client = new Client();
        client.setId(1L);
        client.setLimitBalance(new BigDecimal("100.00"));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        BigDecimal result = clientService.getBalanceClient(1L);
        assertEquals(new BigDecimal("100.00"), result);
    }

    @Test
    void shouldReturnBalancePrepaid() {
        Client client = new Client();
        client.setId(1L);
        client.setPlanType(PlanEnum.PREPAID);
        client.setBalance(new BigDecimal("100.00"));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        BigDecimal result = clientService.getBalanceClient(1L);
        assertEquals(new BigDecimal("100.00"), result);
    }

    @Test
    void shouldReturnClientByDocument() {
        Client client = new Client();
        client.setDocumentId("123456");
        when(clientRepository.findByDocumentId("123456")).thenReturn(Optional.of(client));
        Client result = clientService.getClientByDocument("123456");
        assertEquals("123456", result.getDocumentId());
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> clientService.findById(999L));
    }

    @Test
    void shouldCreatePrepaidClientSuccessfully() {
        ClientRequestDTO request = new ClientRequestDTO();
        request.setName("João");
        request.setDocumentId("12345678900");
        request.setDocumentType("CPF");
        request.setPlanType("PREPAID");
        request.setBalance(BigDecimal.valueOf(100.0));
        request.setActive(true);

        Client clientToSave = Client.fromDTO(request);
        clientToSave.setId(1L);

        when(clientRepository.save(any(Client.class))).thenReturn(clientToSave);

        ClientResponseDTO result = clientService.createClient(request);

        assertNotNull(result);
        assertEquals("João", result.getName());
        assertEquals(BigDecimal.valueOf(100.0), result.getBalance());
        assertNull(result.getLimit());
    }

    @Test
    void shouldThrowExceptionWhenPrepaidHasLimitSet() {
        ClientRequestDTO request = new ClientRequestDTO();
        request.setName("João");
        request.setDocumentId("12345678900");
        request.setDocumentType("CPF");
        request.setPlanType("PREPAID");
        request.setBalance(BigDecimal.valueOf(100.0));
        request.setLimit(BigDecimal.valueOf(500.0));
        request.setActive(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            clientService.createClient(request);
        });

        assertTrue(thrown.getMessage().contains("For PREPAID, balance must be informed and limit null."));
    }

    @Test
    void shouldCreatePospaidClientSuccessfully() {
        ClientRequestDTO request = new ClientRequestDTO();
        request.setName("João");
        request.setDocumentId("12345678900");
        request.setDocumentType("CPF");
        request.setPlanType("POSPAID");
        request.setLimit(BigDecimal.valueOf(100.0));
        request.setActive(true);

        Client clientToSave = Client.fromDTO(request);
        clientToSave.setId(1L);

        when(clientRepository.save(any(Client.class))).thenReturn(clientToSave);

        ClientResponseDTO result = clientService.createClient(request);

        assertNotNull(result);
        assertEquals("João", result.getName());
        assertEquals(BigDecimal.valueOf(100.0), result.getLimit());
        assertNull(result.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenPospaidHasBalanceSet() {
        ClientRequestDTO request = new ClientRequestDTO();
        request.setName("João");
        request.setDocumentId("12345678900");
        request.setDocumentType("CPF");
        request.setPlanType("POSPAID");
        request.setBalance(BigDecimal.valueOf(100.0));
        request.setLimit(BigDecimal.valueOf(500.0));
        request.setActive(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            clientService.createClient(request);
        });

        assertTrue(thrown.getMessage().contains("For POSTPAID, limit must be informed and balance null."));
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenCpfInvalid() {
        ClientRequestDTO request = new ClientRequestDTO();
        request.setName("João");
        request.setDocumentId("123");
        request.setDocumentType("CPF");
        request.setPlanType("POSPAID");
        request.setLimit(BigDecimal.valueOf(500.0));
        request.setActive(true);

        InvalidDocumentException thrown = assertThrows(InvalidDocumentException.class, () -> {
            clientService.createClient(request);
        });

        assertTrue(thrown.getMessage().contains("Invalid document for type: CPF"));
    }

    @Test
    void shouldThrowInvalidDocumentExceptionWhenCnpjInvalid() {
        ClientRequestDTO request = new ClientRequestDTO();
        request.setName("João");
        request.setDocumentId("1234567890123");
        request.setDocumentType("CNPJ");
        request.setPlanType("POSPAID");
        request.setLimit(BigDecimal.valueOf(500.0));
        request.setActive(true);

        InvalidDocumentException thrown = assertThrows(InvalidDocumentException.class, () -> {
            clientService.createClient(request);
        });

        assertTrue(thrown.getMessage().contains("Invalid document for type: CNPJ"));
    }
}

