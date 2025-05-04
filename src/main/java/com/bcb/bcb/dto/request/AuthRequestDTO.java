package com.bcb.bcb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthRequestDTO {

    @Schema(description = "Documento de identificação do cliente (CPF ou CNPJ)", example = "12345678901")
    private String documentId;

}
