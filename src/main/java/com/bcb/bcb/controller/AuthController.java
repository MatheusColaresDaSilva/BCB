package com.bcb.bcb.controller;


import com.bcb.bcb.dto.request.AuthRequestDTO;
import com.bcb.bcb.dto.response.AuthResponseDTO;
import com.bcb.bcb.entity.Client;
import com.bcb.bcb.exception.ClientNotFoundException;
import com.bcb.bcb.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@AllArgsConstructor
public class AuthController extends BaseController{

    private ClientService clientService;

    @Operation(
            summary = "Autentica o cliente",
            description = "Endpoint para autenticar um cliente com base no documentId.(EXEMPLO) O token gerado é a string invertida do documentId."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Documento inválido"),
            @ApiResponse(responseCode = "401", description = "Cliente não encontrado")
    })
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
