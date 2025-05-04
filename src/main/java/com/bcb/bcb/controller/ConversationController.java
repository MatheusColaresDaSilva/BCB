package com.bcb.bcb.controller;


import com.bcb.bcb.dto.response.ConversationResponseDTO;
import com.bcb.bcb.dto.response.MessageResponseDTO;
import com.bcb.bcb.service.ConversationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/conversations")
@AllArgsConstructor
public class ConversationController extends BaseController {

    private final ConversationService conversationService;

    @Operation(
            summary = "Lista conversas do cliente logado",
            description = "Retorna todas as conversas associadas ao cliente atualmente autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversas listadas com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ConversationResponseDTO>> listConversations() {
        List<ConversationResponseDTO> conversations = conversationService.getConversationsLoggedClient();
        return ResponseEntity.ok(conversations);
    }

    @Operation(
            summary = "Obtém detalhes de uma conversa",
            description = "Retorna os detalhes de uma conversa específica pelo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhes da conversa retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conversa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConversationResponseDTO> getConversation(
            @Parameter(description = "ID da conversa", required = true, example = "1")
            @PathVariable Long id
    ) {
        ConversationResponseDTO conversation = conversationService.getConversationDetails(id);
        return ResponseEntity.ok(conversation);
    }

    @Operation(
            summary = "Lista mensagens de uma conversa",
            description = "Retorna todas as mensagens associadas a uma conversa específica pelo ID da conversa."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensagens da conversa listadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conversa não encontrada")
    })
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesFromConversation(
            @Parameter(description = "ID da conversa", required = true, example = "1")
            @PathVariable Long id
    ) {
        List<MessageResponseDTO> messages = conversationService.getMessagesByConversationId(id);
        return ResponseEntity.ok(messages);
    }
}

