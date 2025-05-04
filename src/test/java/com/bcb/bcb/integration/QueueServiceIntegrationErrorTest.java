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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Disabled("Ignorando este teste durante o build CORRIGIR TESTE")
class QueueServiceIntegrationErrorTest extends BasicIntegrationTest{

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
    void shouldPrioritizeUrgentMessageOverNormalAndCheckDeadLetterQueue() throws InterruptedException {
        Client sender = new Client();
        sender.setName("Sender");
        sender.setDocumentId("12345678900");
        sender.setDocumentType(DocumentEnum.CPF);
        sender.setLimitBalance(BigDecimal.valueOf(0.60));
        sender.setPlanType(PlanEnum.POSPAID);
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

        Message normalMessage = new Message();
        normalMessage.setSender(sender);
        normalMessage.setRecipient(recipient);
        normalMessage.setContent("Normal message");
        normalMessage.setStatus(StatusMessageEnum.QUEUED);
        normalMessage.setPriority(PriorityEnum.NORMAL);
        normalMessage.setTimestamp(LocalDateTime.now());
        normalMessage.setCost(BigDecimal.valueOf(0.25));
        normalMessage.setConversation(conversation);
        normalMessage = messageRepository.save(normalMessage);

        Message urgentMessage = new Message();
        urgentMessage.setSender(sender);
        urgentMessage.setRecipient(recipient);
        urgentMessage.setContent("Urgent message");
        urgentMessage.setStatus(StatusMessageEnum.QUEUED);
        urgentMessage.setPriority(PriorityEnum.URGENT);
        urgentMessage.setTimestamp(LocalDateTime.now());
        urgentMessage.setCost(BigDecimal.valueOf(0.50));
        urgentMessage.setConversation(conversation);
        urgentMessage = messageRepository.save(urgentMessage);


        priorityQueue.addMessage(normalMessage);
        priorityQueue.addMessage(urgentMessage);

        Message firstInQueue = priorityQueue.peekMessage();

        Assertions.assertEquals(PriorityEnum.URGENT, firstInQueue.getPriority());
        Assertions.assertEquals("Urgent message", firstInQueue.getContent());

        queueService.processQueue();
        Thread.sleep(10000);

        Message processedMessage = messageRepository.findById(normalMessage.getId()).orElseThrow();
        Client updatedSender = clientRepository.findById(sender.getId()).orElseThrow();

        Assertions.assertEquals(StatusMessageEnum.FAILED, processedMessage.getStatus());
        Assertions.assertEquals(BigDecimal.valueOf(0.1).setScale(2), updatedSender.getLimitBalance());
        Assertions.assertEquals(1, deadLetterQueue.size());
        Assertions.assertEquals(1, readedQueue.size());
    }

}