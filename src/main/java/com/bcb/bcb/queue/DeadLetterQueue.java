package com.bcb.bcb.queue;

import com.bcb.bcb.entity.Message;
import com.bcb.bcb.enums.StatusMessageEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Component
@Qualifier("deadLetterQueue")
public class DeadLetterQueue implements MessageQueueStrategy{

    PriorityQueue<Message> messageQueue = new PriorityQueue<>(new MessageComparable());

    public void addMessage(Message message) {
        message.setStatus(StatusMessageEnum.FAILED);
        messageQueue.add(message);
    }

    public Message pollMessage() {
        return messageQueue.poll();
    }

    public boolean isEmpty() {
        return messageQueue.isEmpty();
    }

    public int size() {
        return messageQueue.size();
    }

    public String print() {

        List<Message> orderedMessages = new ArrayList<>(messageQueue);
        orderedMessages.sort(new MessageComparable());

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < orderedMessages.size(); i++) {
            Message message = orderedMessages.get(i);
            builder.append("Message #" + (i + 1) + ": " + message.toString() + "\n");
        }

        return builder.toString();

    }
}
