package com.bcb.bcb.service;

import com.bcb.bcb.dto.response.ConversationResponseDTO;
import com.bcb.bcb.dto.response.MessageResponseDTO;
import com.bcb.bcb.entity.Client;
import com.bcb.bcb.entity.Conversation;
import com.bcb.bcb.entity.Message;
import com.bcb.bcb.enums.DocumentEnum;
import com.bcb.bcb.enums.PlanEnum;
import com.bcb.bcb.enums.StatusMessageEnum;
import com.bcb.bcb.exception.ConversationNotFoundException;
import com.bcb.bcb.repository.ConversationRepository;
import com.bcb.bcb.repository.MessageRepository;
import com.bcb.bcb.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ConversationService conversationService;

    @Test
    void shouldReturnConversationsForLoggedClient() {
        Long clientId = 1L;
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Matheus");
        client1.setDocumentType(DocumentEnum.CPF);
        client1.setPlanType(PlanEnum.PREPAID);

        Client client2 = new Client();
        client2.setId(1L);
        client2.setName("Fulano");
        client2.setDocumentType(DocumentEnum.CPF);
        client2.setPlanType(PlanEnum.PREPAID);

        Conversation conversation = new Conversation();
        conversation.setClient(client1);
        conversation.setRecipient(client2);
        conversation.setMessages(List.of(new Message()));

        List<Conversation> conversations = List.of(conversation);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUserId).thenReturn(clientId);
            when(conversationRepository.findByClientId(clientId)).thenReturn(conversations);

            List<ConversationResponseDTO> result = conversationService.getConversationsLoggedClient();

            assertEquals(1, result.size());
            verify(conversationRepository).findByClientId(clientId);
        }
    }

    @Test
    void shouldReturnConversationDetails() {
        Long conversationId = 1L;
        Conversation conversation = new Conversation();
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Matheus");

        Client client2 = new Client();
        client2.setId(2L);
        client2.setName("Fulano");

        conversation.setClient(client1);
        conversation.setRecipient(client2);
        conversation.setMessages(List.of(new Message()));

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

        ConversationResponseDTO result = conversationService.getConversationDetails(conversationId);

        assertNotNull(result);
        assertEquals(conversation.getClient().getId(), result.getClientId());
        assertEquals(conversation.getRecipient().getId(), result.getRecipientId());
        verify(conversationRepository).findById(conversationId);
    }


    @Test
    void shouldReturnMessagesByConversationId() {
        Long conversationId = 1L;
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Matheus");

        Message m1 = new Message();
        m1.setId(1L);
        m1.setContent("Teste");
        m1.setCost(new BigDecimal(0.23));
        m1.setSender(client1);
        m1.setStatus(StatusMessageEnum.PROCESSING);

        List<Message> messages = List.of(m1,m1);

        when(messageRepository.findByConversationId(conversationId)).thenReturn(messages);

        List<MessageResponseDTO> result = conversationService.getMessagesByConversationId(conversationId);

        assertEquals(2, result.size());
        verify(messageRepository).findByConversationId(conversationId);
    }

    @Test
    void shouldResolveExistingConversation() {
        Long conversationId = 1L;
        Conversation existingConversation = new Conversation();
        Client sender = new Client();
        Client recipient = new Client();

        existingConversation.setClient(sender);
        existingConversation.setRecipient(recipient);

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(existingConversation));

        Conversation result = conversationService.resolveConversation(conversationId, sender, recipient);

        assertEquals(existingConversation, result);
        verify(conversationRepository).findById(conversationId);
    }

    @Test
    void shouldCreateNewConversationWhenIdIsNull() {
        Client sender = new Client();
        Client recipient = new Client();
        Conversation savedConversation = new Conversation();
        savedConversation.setClient(sender);
        savedConversation.setRecipient(recipient);

        when(conversationRepository.save(any(Conversation.class))).thenReturn(savedConversation);

        Conversation result = conversationService.resolveConversation(null, sender, recipient);

        assertEquals(savedConversation, result);
        verify(conversationRepository).save(any(Conversation.class));
    }


    @Test
    void shouldThrowWhenConversationNotFound() {
        Long id = 999L;

        when(conversationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ConversationNotFoundException.class, () -> {
            conversationService.findById(id);
        });
    }

}