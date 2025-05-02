package com.bcb.bcb.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRequestDTO {
    private String name;
    private String documentId;
    private String documentType;
    private String planType;
    private BigDecimal balance;
    private BigDecimal limit;
    private Boolean active;
}
