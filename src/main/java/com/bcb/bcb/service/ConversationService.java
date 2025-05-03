package com.bcb.bcb.service;

import com.bcb.bcb.dto.response.ConversationResponseDTO;
import com.bcb.bcb.dto.response.MessageResponseDTO;
import com.bcb.bcb.entity.Client;
import com.bcb.bcb.entity.Conversation;
import com.bcb.bcb.entity.Message;
import com.bcb.bcb.exception.ConversationNotFoundException;
import com.bcb.bcb.repository.ConversationRepository;
import com.bcb.bcb.repository.MessageRepository;
import com.bcb.bcb.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public List<ConversationResponseDTO> getConversationsLoggedClient() {
        Long clientId = SecurityUtils.getUserId();
        List<Conversation> conversations = conversationRepository.findByClientId(clientId);
        return conversations.stream()
                .map(ConversationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ConversationResponseDTO getConversationDetails(Long conversationId) {
        Conversation conversation = findById(conversationId);
        return ConversationResponseDTO.fromEntity(conversation);
    }

    public List<MessageResponseDTO> getMessagesByConversationId(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        return messages.stream()
                .map(MessageResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Conversation resolveConversation(Long conversationId, Client sender, Client recipient) {
        if (conversationId != null) {
            return findById(conversationId);
        }

        Conversation newConversation = new Conversation();
        newConversation.setClient(sender);
        newConversation.setRecipient(recipient);
        return conversationRepository.save(newConversation);
    }

    public Conversation findById(Long id) {
        return conversationRepository.findById(id).orElseThrow(() -> new ConversationNotFoundException());
    }
}
