package com.bcb.bcb.queue;

import com.bcb.bcb.dto.response.StatusQueueDTO;
import com.bcb.bcb.entity.Message;

public interface MessageQueueStrategy {
    Message pollMessage();
    void addMessage(Message message);
    int size();
    String print();
    boolean isEmpty();

    default StatusQueueDTO status() {
        return StatusQueueDTO.builder().size(this.size()).build();
    }

}