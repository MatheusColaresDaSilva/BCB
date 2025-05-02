package com.bcb.bcb.utils;

import com.bcb.bcb.exception.InvalidDocumentException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateCpf implements ValidateDocumentInterface{

    @Override
    public Boolean valid(String document) {
        return validator(document);
    }

    private static boolean validator(String field) {
        Pattern pattern = Pattern.compile("^\\d{11}$");
        Matcher matcher = pattern.matcher(field);
        if(!matcher.find()) {
            throw new InvalidDocumentException("CPF");
        }

        return true;
    }
}
