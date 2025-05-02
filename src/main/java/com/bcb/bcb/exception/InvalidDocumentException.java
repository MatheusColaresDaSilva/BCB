package com.bcb.bcb.exception;

public class InvalidDocumentException extends BusinessException{

    public InvalidDocumentException(String string) {
        super("Invalid document for type: " + string);
    }
}
