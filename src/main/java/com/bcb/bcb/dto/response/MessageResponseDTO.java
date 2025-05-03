package com.bcb.bcb.dto.response;

import com.bcb.bcb.entity.Client;
import com.bcb.bcb.entity.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Long id;
    private String status;
    private LocalDateTime estimatedDelivery;
    private BigDecimal cost;
    private BigDecimal currentBalance;


    public static MessageResponseDTO fromEntity(Message message) {
        Client sender = message.getSender();

        return MessageResponseDTO.builder()
                .id(message.getId())
                .status(message.getStatus().name().toLowerCase())
                .estimatedDelivery(message.getTimestamp())
                .cost(message.getCost())
                .currentBalance(sender.isPrePaid() &&sender.getBalance() != null ? sender.getBalance() : null)
                .build();
    }

}
