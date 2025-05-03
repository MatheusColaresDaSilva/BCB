package com.bcb.bcb.queue;

import com.bcb.bcb.entity.Message;

public interface MessageQueueStrategy {
    Message pollMessage();
    void addMessage(Message message);
    String print();
    boolean isEmpty();
}