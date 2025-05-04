package com.bcb.bcb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequestDTO {
    @Schema(description = "ID da conversa associada à mensagem. Sem não informado, uma nova conversa será criada", example = "12345")
    private Long conversationId;

    @Schema(description = "ID do destinatário da mensagem", example = "67890")
    private Long recipientId;

    @Schema(description = "Conteúdo da mensagem", example = "Olá, como você está?")
    private String content;

    @Schema(description = "Prioridade da mensagem", example = "NORMAL", allowableValues = {"NORMAL", "URGENT"})
    private String priority;
}
