package com.bcb.bcb.service;

import com.bcb.bcb.dto.request.MessageRequestDTO;
import com.bcb.bcb.dto.response.MessageResponseDTO;
import com.bcb.bcb.entity.Client;
import com.bcb.bcb.entity.Conversation;
import com.bcb.bcb.entity.Message;
import com.bcb.bcb.enums.PriorityEnum;
import com.bcb.bcb.enums.StatusMessageEnum;
import com.bcb.bcb.exception.MessageNotFoundException;
import com.bcb.bcb.queue.PriorityMessageQueue;
import com.bcb.bcb.repository.ConversationRepository;
import com.bcb.bcb.repository.MessageRepository;
import com.bcb.bcb.specification.builder.MessageSpecificationsBuilder;
import com.bcb.bcb.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private MessageRepository messageRepository;
    private ConversationRepository conversationRepository;
    private ClientService clientService;
    private PriorityMessageQueue priorityMessageQueue;

    @Transactional
    public MessageResponseDTO sendMessage(MessageRequestDTO messageRequestDTO) {
        Long loggedUserId = SecurityUtils.getUserId();

        Client sender = clientService.getClientById(loggedUserId);

        Client recipient = clientService.getClientById(messageRequestDTO.getRecipientId());

        Conversation conversation = resolveConversation(messageRequestDTO.getConversationId(), sender, recipient);

        PriorityEnum priority = PriorityEnum.valueOf(messageRequestDTO.getPriority().toUpperCase());

        BigDecimal cost = priority == PriorityEnum.URGENT ? new BigDecimal("0.50") : new BigDecimal("0.25");

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(messageRequestDTO.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setPriority(priority);
        message.setStatus(StatusMessageEnum.QUEUED);
        message.setCost(cost);

        messageRepository.save(message);

        priorityMessageQueue.addMessage(message);

        return new MessageResponseDTO(
                message.getId(),
                message.getStatus().name().toLowerCase(),
                message.getTimestamp(),
                message.getCost(),
                sender.getBalance() != null ? sender.getBalance().subtract(cost) : null
        );
    }


    private Conversation resolveConversation(Long conversationId, Client sender, Client recipient) {
        if (conversationId != null) {
            return conversationRepository.findById(conversationId)
                    .orElseThrow(() -> new EntityNotFoundException("Conversa não encontrada"));
        }

        Conversation newConversation = new Conversation();
        newConversation.setClient(sender);
        newConversation.setRecipient(recipient);
        return conversationRepository.save(newConversation);
    }


    public List<MessageResponseDTO> listMessagesWithFilters(String search) {
        MessageSpecificationsBuilder builder = new MessageSpecificationsBuilder();
        builder.with(search);
        Specification<Message> spec = builder.build();
        return messageRepository.findAll(spec).stream().map(MessageResponseDTO::fromEntity).toList();

    }

    public MessageResponseDTO getMessageById(Long id) {
        Message message = findById(id);
        return MessageResponseDTO.fromEntity(message);
    }

    public String getMessageStatus(Long id) {
        Message message = findById(id);
        return message.getStatus().name().toLowerCase();
    }

    public Message findById(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> new MessageNotFoundException());
    }
}
