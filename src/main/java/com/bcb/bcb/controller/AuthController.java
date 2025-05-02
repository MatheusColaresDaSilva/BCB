package com.bcb.bcb.controller;


import com.bcb.bcb.dto.request.AuthRequestDTO;
import com.bcb.bcb.dto.request.ClientRequestDTO;
import com.bcb.bcb.dto.response.AuthResponseDTO;
import com.bcb.bcb.dto.response.ClientResponseDTO;
import com.bcb.bcb.dto.response.ResponseDTO;
import com.bcb.bcb.entity.Client;
import com.bcb.bcb.enums.DocumentEnum;
import com.bcb.bcb.exception.ClientNotFoundException;
import com.bcb.bcb.exception.InvalidDocumentException;
import com.bcb.bcb.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class AuthController extends BaseController{

    private ClientService clientService;

    public AuthController(ClientService clientService) {
        this.clientService = clientService;
    }


    @PostMapping("/auth")
    public ResponseEntity<?> createClient(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            Client client = clientService.getClientByDocument(authRequestDTO.getDocumentId());

            if (!client.getDocumentType().isValid(client.getDocumentId())) {
                return ResponseEntity.badRequest().body("Invalid Document");
            }
        } catch (ClientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Client Not Founded");
        }

        return ResponseEntity.ok(new AuthResponseDTO(new StringBuilder(authRequestDTO.getDocumentId()).reverse().toString()));

    }
}
