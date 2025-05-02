package com.bcb.bcb.enums;

import com.bcb.bcb.utils.ValidateCnpj;
import com.bcb.bcb.utils.ValidateCpf;
import com.bcb.bcb.utils.ValidateDocumentInterface;

import java.util.Arrays;

public enum DocumentEnum {

    CPF("CPF", new ValidateCpf()),
    CNPJ("CNPJ", new ValidateCnpj());

    private final String code;
    private final ValidateDocumentInterface validator;

    DocumentEnum(String code, ValidateDocumentInterface validator) {
        this.code = code;
        this.validator = validator;
    }

    public String getCode() {
        return code;
    }

    public boolean isValid(String document) {
        return validator.valid(document);
    }

    public static DocumentEnum fromCode(String code) {
        return Arrays.stream(DocumentEnum.values())
                .filter(e -> e.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid document type: " + code));
    }


}
