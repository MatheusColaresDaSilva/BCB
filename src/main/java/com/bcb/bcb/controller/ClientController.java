package com.bcb.bcb.controller;


import com.bcb.bcb.dto.request.ClientRequestDTO;
import com.bcb.bcb.dto.response.ClientResponseDTO;
import com.bcb.bcb.dto.response.ResponseDTO;
import com.bcb.bcb.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/clients")
@AllArgsConstructor
public class ClientController extends BaseController {

    private ClientService clientService;

    @Operation(summary = "Buscar todos os clientes")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    @GetMapping
    public ResponseEntity<ResponseDTO<List<ClientResponseDTO>>> findAll() {
        List<ClientResponseDTO> response = clientService.findAll();
        return ResponseEntity.ok(new ResponseDTO<>(response));
    }

    @Operation(summary = "Criar um novo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ResponseDTO<ClientResponseDTO>> createClient(
            @RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO response = clientService.createClient(clientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(response));
    }

    @Operation(summary = "Buscar cliente por ID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ClientResponseDTO>> findById(
            @Parameter(description = "ID do cliente") @PathVariable Long id) {
        ClientResponseDTO response = clientService.findById(id);
        return ResponseEntity.ok(new ResponseDTO<>(response));
    }

    @Operation(summary = "Atualizar cliente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ClientResponseDTO>> updateClient(
            @RequestBody ClientRequestDTO clientRequestDTO,
            @Parameter(description = "ID do cliente") @PathVariable Long id) {
        ClientResponseDTO response = clientService.updateClient(clientRequestDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(response));
    }

    @Operation(summary = "Obter saldo do cliente")
    @GetMapping("/{id}/balance")
    public ResponseEntity<ResponseDTO<BigDecimal>> getBalanceClient(
            @Parameter(description = "ID do cliente") @PathVariable Long id) {
        BigDecimal response = clientService.getBalanceClient(id);
        return ResponseEntity.ok(new ResponseDTO<>(response));
    }
}

