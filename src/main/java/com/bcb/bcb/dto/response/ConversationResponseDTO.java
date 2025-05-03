package com.bcb.bcb.dto.response;

import com.bcb.bcb.entity.Conversation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponseDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long recipientId;
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
