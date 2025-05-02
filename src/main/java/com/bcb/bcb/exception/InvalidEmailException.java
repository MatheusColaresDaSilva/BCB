package com.bcb.bcb.exception;

public class InvalidEmailException extends BusinessException{

    public InvalidEmailException() {
        super("Email Inválido");
    }
}
