package com.bcb.bcb.controller;


import com.bcb.bcb.dto.response.ResponseDTO;
import com.bcb.bcb.dto.response.StatusQueueDTO;
import com.bcb.bcb.queue.MessageQueueFactory;
import com.bcb.bcb.queue.MessageQueueStrategy;
import com.bcb.bcb.service.QueueService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/queue")
@AllArgsConstructor
public class QueueController extends BaseController {

    private QueueService queueService;
    private MessageQueueFactory messageQueueFactory;

    @Operation(
            summary = "Obtém a fila com base no tipo",
            description = "Esse endpoint retorna a representação em string de uma fila específica, " +
                    "baseada no tipo fornecido no parâmetro 'type'."
    )
    @GetMapping("/print")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fila impressa com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo de fila inválido")
    })
    public ResponseEntity<String> getQueue(
            @Parameter(description = "Tipo da fila a ser retornada", required = true, example = "priority | deadletter | readed" )
            @RequestParam String type
    ) {

        MessageQueueStrategy queue = messageQueueFactory.getQueue(type.toLowerCase());
        return ResponseEntity.ok(queue.print());
    }

    @Operation(
            summary = "Obtém o status da fila",
            description = "Esse endpoint retorna o status de uma fila com base no tipo fornecido no parâmetro 'type'."
    )
    @GetMapping("/status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da fila retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo de fila inválido")
    })
    public ResponseEntity<ResponseDTO<StatusQueueDTO>> getStatus(
            @Parameter(description = "Tipo da fila para consulta de status", required = true, example = "priority | deadletter | readed")
            @RequestParam String type
    ) {
        MessageQueueStrategy queue = messageQueueFactory.getQueue(type.toLowerCase());
        return ResponseEntity.ok(new ResponseDTO<>(queue.status()));
    }

    @Operation(
            summary = "Processa a fila",
            description = "Esse endpoint inicia o processamento da fila."
    )
    @PostMapping("/process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Processamento da fila iniciado com sucesso")
    })
    public ResponseEntity<String> processQueue() {
        queueService.processQueue();
        return ResponseEntity.ok("Queue processing started");
    }
}

