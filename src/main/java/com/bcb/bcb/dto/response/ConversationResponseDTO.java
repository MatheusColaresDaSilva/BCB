package com.bcb.bcb.dto.response;

import com.bcb.bcb.entity.Conversation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponseDTO {
    @Schema(description = "ID único da conversa", example = "101")
    private Long id;

    @Schema(description = "ID do cliente participante da conversa", example = "10")
    private Long clientId;

    @Schema(description = "Nome do cliente", example = "João da Silva")
    private String clientName;

    @Schema(description = "ID do destinatário da conversa", example = "20")
    private Long recipientId;

    @Schema(description = "Nome do destinatário", example = "Atendente Virtual")
    private String recipientName;

    public static ConversationResponseDTO fromEntity(Conversation conversation) {
        return ConversationResponseDTO.builder()
                .id(conversation.getId())
                .clientId(conversation.getClient().getId())
                .clientName(conversation.getClient().getName())
                .recipientId(conversation.getRecipient().getId())
                .recipientName(conversation.getRecipient().getName())
                .build();
    }
}
