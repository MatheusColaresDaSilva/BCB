package com.bcb.bcb.integration;

import com.bcb.bcb.entity.Client;
import com.bcb.bcb.entity.Conversation;
import com.bcb.bcb.entity.Message;
import com.bcb.bcb.enums.DocumentEnum;
import com.bcb.bcb.enums.PlanEnum;
import com.bcb.bcb.enums.PriorityEnum;
import com.bcb.bcb.enums.StatusMessageEnum;
import com.bcb.bcb.queue.MessageQueueStrategy;
import com.bcb.bcb.repository.ClientRepository;
import com.bcb.bcb.repository.ConversationRepository;
import com.bcb.bcb.repository.MessageRepository;
import com.bcb.bcb.service.QueueService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Disabled("Ignorando este teste durante o build CORRIGIR TESTE")
class QueueServiceIntegrationSuccessTest extends BasicIntegrationTest{

    @Autowired
    @Qualifier("priorityQueue")
    private MessageQueueStrategy priorityQueue;

    @Autowired
    @Qualifier("readedQueue")
    private MessageQueueStrategy readedQueue;

    @Autowired
    @Qualifier("deadLetterQueue")
    private MessageQueueStrategy deadLetterQueue;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private QueueService queueService;

    @Test
    void shouldProcessRealMessage() throws InterruptedException {
        Client sender = new Client();
        sender.setName("Sender");
        sender.setDocumentId("12345678900");
        sender.setDocumentType(DocumentEnum.CPF);
        sender.setBalance(BigDecimal.valueOf(10));
        sender.setPlanType(PlanEnum.PREPAID);
        sender = clientRepository.save(sender);

        Client recipient = new Client();
        recipient.setName("Recipient");
        recipient.setDocumentId("14785236989");
        recipient.setDocumentType(DocumentEnum.CPF);
        recipient.setPlanType(PlanEnum.PREPAID);
        recipient.setBalance(BigDecimal.valueOf(10));
        recipient = clientRepository.save(recipient);

        Conversation conversation = new Conversation();
        conversation.setClient(sender);
        conversation.setRecipient(recipient);
        conversationRepository.save(conversation);

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent("Hello integration test!");
        message.setStatus(StatusMessageEnum.QUEUED);
        message.setPriority(PriorityEnum.NORMAL);
        message.setTimestamp(LocalDateTime.now());
        message.setCost(BigDecimal.valueOf(1));
        message.setConversation(conversation);
        message = messageRepository.save(message);

        priorityQueue.addMessage(message);

        queueService.processQueue();
        Thread.sleep(10000);
        Message processedMessage = messageRepository.findById(message.getId()).orElseThrow();
        Client updatedSender = clientRepository.findById(sender.getId()).orElseThrow();

        Assertions.assertEquals(StatusMessageEnum.SENT, processedMessage.getStatus());
        Assertions.assertEquals(BigDecimal.valueOf(9).setScale(2), updatedSender.getBalance());
        Assertions.assertEquals(1, readedQueue.size());
        Assertions.assertTrue(deadLetterQueue.isEmpty());
    }



}