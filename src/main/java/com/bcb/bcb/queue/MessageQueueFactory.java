package com.bcb.bcb.queue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageQueueFactory {
    private final Map<String, MessageQueueStrategy> queueMap;

    public MessageQueueFactory(
            @Qualifier("priorityQueue") MessageQueueStrategy priorityQueue,
            @Qualifier("deadLetterQueue") MessageQueueStrategy deadLetterQueue,
            @Qualifier("readedQueue") MessageQueueStrategy readedQueue
    ) {
        queueMap = Map.of(
                "priority", priorityQueue,
                "deadletter", deadLetterQueue,
                "readed", readedQueue
        );
    }

    public MessageQueueStrategy getQueue(String type) {
        return queueMap.get(type.toLowerCase());
    }
}