package com.bcb.bcb.controller;


import com.bcb.bcb.dto.request.MessageRequestDTO;
import com.bcb.bcb.dto.response.MessageResponseDTO;
import com.bcb.bcb.dto.response.ResponseDTO;
import com.bcb.bcb.enums.PriorityEnum;
import com.bcb.bcb.enums.StatusMessageEnum;
import com.bcb.bcb.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@AllArgsConstructor
public class MessageController extends BaseController{

    private MessageService messageService;


    @PostMapping
    public ResponseEntity<ResponseDTO<MessageResponseDTO>> sendMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        MessageResponseDTO response = messageService.sendMessage(messageRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(response));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<MessageResponseDTO>>> listMessages(@RequestParam(required = false) String search) {
        List<MessageResponseDTO> messages = messageService.listMessagesWithFilters(search);
        return ResponseEntity.ok(new ResponseDTO<>(messages));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<MessageResponseDTO>> getMessageById(@PathVariable Long id) {
        MessageResponseDTO message = messageService.getMessageById(id);
        return ResponseEntity.ok(new ResponseDTO<>(message));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<String>> getMessageStatus(@PathVariable Long id) {
        String status = messageService.getMessageStatus(id);
        return ResponseEntity.ok(new ResponseDTO<>(status));
    }

}
