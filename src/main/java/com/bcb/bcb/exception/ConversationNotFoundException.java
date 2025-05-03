package com.bcb.bcb.exception;

public class ConversationNotFoundException extends BusinessException{

    public ConversationNotFoundException() {
        super("Conversation Not Found");
    }
}
