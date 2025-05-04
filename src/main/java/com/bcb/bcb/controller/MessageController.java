package com.bcb.bcb.controller;


import com.bcb.bcb.dto.request.MessageRequestDTO;
import com.bcb.bcb.dto.response.MessageResponseDTO;
import com.bcb.bcb.dto.response.ResponseDTO;
import com.bcb.bcb.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@AllArgsConstructor
public class MessageController extends BaseController {

    private MessageService messageService;

    @Operation(summary = "Envia uma nova mensagem", description = "Esse endpoint envia uma nova mensagem para o destinatário.")
    @PostMapping
    public ResponseEntity<ResponseDTO<MessageResponseDTO>> sendMessage(
            @RequestBody MessageRequestDTO messageRequestDTO
    ) {
        MessageResponseDTO response = messageService.sendMessage(messageRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(response));
    }

    @Operation(
            summary = "Lista todas as mensagens",
            description = "Esse endpoint retorna uma lista de todas as mensagens enviadas, podendo ser filtradas pelo parâmetro 'search'. " +
                    "O parâmetro 'search' deve seguir o formato: campo operador valor. " +
                    "Exemplo: 'status==SENT,priority!=NORMAL'"
    )
    @GetMapping
    public ResponseEntity<ResponseDTO<List<MessageResponseDTO>>> listMessages(
            @RequestParam
            @Parameter(
                    description = "Os operadores suportados são: EQUALITY(==), NEGATION(!=), " +
                            "GREATER_THAN(>), LESS_THAN(<), GT_EQUAL(>=), LT_EQUAL(<=), CONTAINS(%)",
                    example = "status==SENT"
            )
                    String search
    ) {
        List<MessageResponseDTO> messages = messageService.listMessagesWithFilters(search);
        return ResponseEntity.ok(new ResponseDTO<>(messages));
    }

    @Operation(summary = "Obtém uma mensagem específica", description = "Esse endpoint retorna os detalhes de uma mensagem específica pelo ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<MessageResponseDTO>> getMessageById(
            @PathVariable Long id
    ) {
        MessageResponseDTO message = messageService.getMessageById(id);
        return ResponseEntity.ok(new ResponseDTO<>(message));
    }

    @Operation(summary = "Obtém o status de uma mensagem", description = "Esse endpoint retorna o status atual da mensagem.")
    @GetMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<String>> getMessageStatus(
            @PathVariable Long id
    ) {
        String status = messageService.getMessageStatus(id);
        return ResponseEntity.ok(new ResponseDTO<>(status));
    }
}