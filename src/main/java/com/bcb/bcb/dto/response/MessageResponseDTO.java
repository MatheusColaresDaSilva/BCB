package com.bcb.bcb.dto.response;

import com.bcb.bcb.entity.Client;
import com.bcb.bcb.entity.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponseDTO {
    @Schema(description = "ID único da mensagem", example = "123")
    private Long id;

    @Schema(description = "Status da mensagem", example = "SENT")
    private String status;

    @Schema(description = "Data estimada para entrega da mensagem", example = "2023-05-04T12:30:00")
    private LocalDateTime estimatedDelivery;

    @Schema(description = "Custo da mensagem", example = "0.50")
    private BigDecimal cost;

    @Schema(description = "Saldo atual do cliente após envio da mensagem", example = "50.00")
    private BigDecimal currentBalance;


    public static MessageResponseDTO fromEntity(Message message) {
        Client sender = message.getSender();

        return MessageResponseDTO.builder()
                .id(message.getId())
                .status(message.getStatus().name().toLowerCase())
                .estimatedDelivery(message.getTimestamp())
                .cost(message.getCost())
                .currentBalance(sender.isPrePaid() && sender.getBalance() != null ? sender.getBalance() : null)
                .build();
    }

}
