package com.bcb.bcb.exception;

public class ClientNotFoundException extends BusinessException{

    public ClientNotFoundException() {
        super("Client Not Found");
    }
}
