package com.bcb.bcb.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequestDTO {
    private Long conversationId;
    private Long recipientId;
    private String content;
    private String priority;
}
