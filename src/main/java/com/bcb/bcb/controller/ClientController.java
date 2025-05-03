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

@RestController
@RequestMapping("/api/v1/clients")
@AllArgsConstructor
public class ClientController extends BaseController{

    private ClientService clientService;

    //TODO roles ADMIN
    @GetMapping
    public ResponseEntity<ResponseDTO<List<ClientResponseDTO>>> findAll() {
        List<ClientResponseDTO> response = clientService.findAll();
        return ResponseEntity.ok(new ResponseDTO<>(response));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ClientResponseDTO>> createClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO response = clientService.createClient(clientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ClientResponseDTO>> findById(@PathVariable Long id) {
        ClientResponseDTO response = clientService.findById(id);
        return ResponseEntity.ok(new ResponseDTO<>(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ClientResponseDTO>> updateClient(@RequestBody ClientRequestDTO clientRequestDTO, @PathVariable Long id) {
        ClientResponseDTO response = clientService.updateClient(clientRequestDTO, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO<>(response));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<ResponseDTO<BigDecimal>> getBalanceClient(@PathVariable Long id) {
        BigDecimal response = clientService.getBalanceClient(id);
        return ResponseEntity.ok(new ResponseDTO<>(response));
    }
}
