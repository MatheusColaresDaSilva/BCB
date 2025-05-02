package com.bcb.bcb.dto.response;

import com.bcb.bcb.entity.Client;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {
    private String name;
    private String documentId;
    private String documentType;
    private String planType;
    private BigDecimal balance;
    private BigDecimal limit;
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
