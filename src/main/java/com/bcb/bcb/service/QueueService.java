package com.bcb.bcb.service;

import com.bcb.bcb.entity.Client;
import com.bcb.bcb.entity.Message;
import com.bcb.bcb.enums.StatusMessageEnum;
import com.bcb.bcb.exception.ClientNotFoundException;
import com.bcb.bcb.queue.PriorityMessageQueue;
import com.bcb.bcb.repository.ClientRepository;
import com.bcb.bcb.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class QueueService {

    private PriorityMessageQueue priorityMessageQueue;
    private ClientRepository clientRepository;
    private MessageRepository messageRepository;

    @Async
    public void processQueue() {

        Message message = priorityMessageQueue.pollMessage();

        try {
            message.setStatus(StatusMessageEnum.PROCESSING);
            Client sender = clientRepository.findById(message.getSender().getId()).orElseThrow(() -> new ClientNotFoundException());
            BigDecimal cost = message.getCost();

            if (sender.canAfford(cost)) {
                sender.debitAmount(cost);
                message.setStatus(StatusMessageEnum.SENT);
                clientRepository.save(sender);
                messageRepository.save(message);

                System.out.println("Processed and debited: " + message);
            } else {
                message.setStatus(StatusMessageEnum.FAILED);
                messageRepository.save(message);
                System.err.println("Insufficient balance, failed: " + message);
            }
        } catch (Exception e) {
            message.setStatus(StatusMessageEnum.FAILED);
            messageRepository.save(message);
            System.err.println("Failed processing message: " + message);
            e.printStackTrace();
        }

    }
}
