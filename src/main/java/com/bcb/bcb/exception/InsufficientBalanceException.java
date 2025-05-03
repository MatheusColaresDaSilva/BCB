package com.bcb.bcb.exception;

public class InsufficientBalanceException extends BusinessException{

    public InsufficientBalanceException() {
        super("Insufficient credit");
    }
}
