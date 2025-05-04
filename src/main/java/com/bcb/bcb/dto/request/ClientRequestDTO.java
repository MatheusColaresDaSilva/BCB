package com.bcb.bcb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRequestDTO {
    @Schema(description = "Nome do cliente", example = "João da Silva")
    private String name;

    @Schema(description = "Número do documento do cliente (CPF ou CNPJ)", example = "12345678900")
    private String documentId;

    @Schema(description = "Tipo do documento", example = "CPF", allowableValues = {"CPF", "CNPJ"})
    private String documentType;

    @Schema(description = "Tipo do plano do cliente", example = "PREPAID", allowableValues = {"PREPAID", "POSPAID"})
    private String planType;

    @Schema(description = "Saldo de crédito disponível (se PREPAID)", example = "100.00")
    private BigDecimal balance;

    @Schema(description = "Limite de crédito do cliente (se POSPAID)", example = "50.00")
    private BigDecimal limit;

    @Schema(description = "Indica se o cliente está ativo", example = "true")
    private Boolean active;
}
