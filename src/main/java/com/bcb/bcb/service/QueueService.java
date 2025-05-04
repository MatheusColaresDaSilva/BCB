package com.bcb.bcb.service;

import com.bcb.bcb.entity.Client;
import com.bcb.bcb.entity.Message;
import com.bcb.bcb.enums.StatusMessageEnum;
import com.bcb.bcb.exception.ClientNotFoundException;
import com.bcb.bcb.queue.MessageQueueStrategy;
import com.bcb.bcb.repository.ClientRepository;
import com.bcb.bcb.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class QueueService {

    @Qualifier("priorityQueue")
    private final MessageQueueStrategy priorityMessageQueue;

    @Qualifier("deadLetterQueue")
    private final MessageQueueStrategy deadLetterQueue;

    @Qualifier("readedQueue")
    private final MessageQueueStrategy readedQueue;


    private ClientRepository clientRepository;
    private MessageRepository messageRepository;

    @Async
    public void processQueue() {

        while (!priorityMessageQueue.isEmpty()) {
            Message message = priorityMessageQueue.pollMessage();

            try {
                message.setStatus(StatusMessageEnum.PROCESSING);
                Client sender = clientRepository.findById(message.getSender().getId())
                        .orElseThrow(ClientNotFoundException::new);

                BigDecimal cost = message.getCost();

                if (sender.canAfford(cost)) {
                    sender.debitAmount(cost);
                    message.setStatus(StatusMessageEnum.SENT);
                    clientRepository.save(sender);
                    messageRepository.save(message);
                    readedQueue.addMessage(message);
                } else {
                    message.setStatus(StatusMessageEnum.FAILED);
                    messageRepository.save(message);
                    deadLetterQueue.addMessage(message);
                }
            } catch (Exception e) {
                message.setStatus(StatusMessageEnum.FAILED);
                messageRepository.save(message);
                deadLetterQueue.addMessage(message);
            }
        }
    }
}
