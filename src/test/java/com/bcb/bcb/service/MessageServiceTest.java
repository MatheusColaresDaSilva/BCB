package com.bcb.bcb.service;

import com.bcb.bcb.dto.request.MessageRequestDTO;
import com.bcb.bcb.dto.response.MessageResponseDTO;
import com.bcb.bcb.entity.Client;
import com.bcb.bcb.entity.Conversation;
import com.bcb.bcb.entity.Message;
import com.bcb.bcb.enums.PlanEnum;
import com.bcb.bcb.enums.StatusMessageEnum;
import com.bcb.bcb.exception.InsufficientBalanceException;
import com.bcb.bcb.exception.MessageNotFoundException;
import com.bcb.bcb.queue.PriorityMessageQueue;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ConversationService conversationService;

    @Mock
    private ClientService clientService;

    @Mock
    private PriorityMessageQueue priorityMessageQueue;

    @InjectMocks
    private MessageService messageService;

    @Test
    void shouldSendMessageWhenClientHasSufficientBalance() {
        Long loggedUserId = 1L;
        Long recipientId = 2L;
        BigDecimal messageCost = new BigDecimal(0.25);

        Client sender = new Client();
        sender.setId(loggedUserId);
        sender.setPlanType(PlanEnum.PREPAID);
        sender.setBalance(BigDecimal.valueOf(100));

        Client recipient = new Client();
        recipient.setId(recipientId);

        MessageRequestDTO messageRequestDTO = new MessageRequestDTO(1L, recipientId, "Test content", "NORMAL");

        Conversation conversation = new Conversation();

        when(clientService.getClientById(loggedUserId)).thenReturn(sender);
        when(clientService.getClientById(recipientId)).thenReturn(recipient);
        when(conversationService.resolveConversation(any(), any(), any())).thenReturn(conversation);
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(priorityMessageQueue).addMessage(any(Message.class));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUserId).thenReturn(loggedUserId);

            MessageResponseDTO response = messageService.sendMessage(messageRequestDTO);
            assertNotNull(response);
            assertEquals(StatusMessageEnum.QUEUED.name().toLowerCase(), response.getStatus());
            assertEquals(sender.getBalance().subtract(messageCost), response.getCurrentBalance());

            verify(messageRepository).save(any(Message.class));
            verify(priorityMessageQueue).addMessage(any(Message.class));
        }

    }

    @Test
    void shouldThrowInsufficientBalanceExceptionWhenClientHasInsufficientBalance() {
        Long loggedUserId = 1L;
        Long recipientId = 2L;

        Client sender = new Client();
        sender.setId(loggedUserId);
        sender.setPlanType(PlanEnum.PREPAID);
        sender.setBalance(BigDecimal.valueOf(0.10));

        Client recipient = new Client();
        recipient.setId(recipientId);

        MessageRequestDTO messageRequestDTO = new MessageRequestDTO(1L, recipientId, "Test content", "NORMAL");

        when(clientService.getClientById(loggedUserId)).thenReturn(sender);
        when(clientService.getClientById(recipientId)).thenReturn(recipient);
        when(conversationService.resolveConversation(any(), any(), any())).thenReturn(new Conversation());

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getUserId).thenReturn(loggedUserId);

            assertThrows(InsufficientBalanceException.class, () -> {
                messageService.sendMessage(messageRequestDTO);
            });
        }
    }

    @Test
    void shouldReturnMessageById() {
        Long messageId = 1L;
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Matheus");

        Message message = new Message();
        message.setId(1L);
        message.setContent("Teste");
        message.setCost(new BigDecimal(0.23));
        message.setSender(client1);
        message.setStatus(StatusMessageEnum.PROCESSING);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        MessageResponseDTO result = messageService.getMessageById(messageId);

        assertNotNull(result);
        assertEquals(messageId, result.getId());
        assertEquals(StatusMessageEnum.PROCESSING.name().toLowerCase(), result.getStatus());
        verify(messageRepository).findById(messageId);
    }

    @Test
    void shouldReturnMessageStatus() {
        Long messageId = 1L;
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Matheus");

        Message message = new Message();
        message.setId(1L);
        message.setContent("Teste");
        message.setCost(new BigDecimal(0.23));
        message.setSender(client1);
        message.setStatus(StatusMessageEnum.QUEUED);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        String result = messageService.getMessageStatus(messageId);

        assertEquals(StatusMessageEnum.QUEUED.name().toLowerCase(), result);
        verify(messageRepository).findById(messageId);
    }

    @Test
    void shouldThrowMessageNotFoundExceptionWhenMessageNotFound() {
        Long messageId = 999L;

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        assertThrows(MessageNotFoundException.class, () -> {
            messageService.findById(messageId);
        });
    }


}
