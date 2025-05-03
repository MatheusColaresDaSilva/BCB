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

@RestController
@RequestMapping("/api/v1/conversations")
@AllArgsConstructor
public class ConversationController extends BaseController{

    private final ConversationService conversationService;

    @GetMapping
    public ResponseEntity<List<ConversationResponseDTO>> listConversations() {
        List<ConversationResponseDTO> conversations = conversationService.getConversationsLoggedClient();
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationResponseDTO> getConversation(@PathVariable Long id) {
        ConversationResponseDTO conversation = conversationService.getConversationDetails(id);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesFromConversation(@PathVariable Long id) {
        List<MessageResponseDTO> messages = conversationService.getMessagesByConversationId(id);
        return ResponseEntity.ok(messages);
    }
}
