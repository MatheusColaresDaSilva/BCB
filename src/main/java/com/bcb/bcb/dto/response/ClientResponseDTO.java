package com.bcb.bcb.dto.response;

import com.bcb.bcb.entity.Client;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {
    @Schema(description = "Nome do cliente")
    private String name;

    @Schema(description = "Documento do cliente (CPF ou CNPJ)")
    private String documentId;

    @Schema(description = "Tipo de documento", example = "CPF", allowableValues = {"CPF", "CNPJ"})
    private String documentType;

    @Schema(description = "Tipo de plano", example = "PREPAID", allowableValues = {"PREPAID", "POSPAID"})
    private String planType;

    @Schema(description = "Saldo de crédito disponível (se PREPAID)")
    private BigDecimal balance;

    @Schema(description = "Limite disponível para o cliente (se POSPAID)")
    private BigDecimal limit;

    @Schema(description = "Cliente ativo ou inativo")
    private Boolean active;

    public static ClientResponseDTO fromEntity(Client client) {
        return ClientResponseDTO.builder()
                .name(client.getName())
                .documentId(client.getDocumentId())
                .documentType(client.getDocumentType().name())
                .planType(client.getPlanType().name())
                .balance(client.getBalance())
                .limit(client.getLimitBalance())
                .active(client.getActive())
                .build();
    }
}
