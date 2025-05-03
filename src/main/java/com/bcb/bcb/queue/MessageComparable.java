package com.bcb.bcb.queue;

import com.bcb.bcb.entity.Message;

import java.util.Comparator;

public class MessageComparable implements Comparator<Message> {
    @Override
    public int compare(Message m1, Message m2) {
        int priorityCompare = Integer.compare(m1.getPriority().getWeigth(), m2.getPriority().getWeigth());
        if (priorityCompare != 0) return priorityCompare;
        return m1.getTimestamp().compareTo(m2.getTimestamp());
    }
}
