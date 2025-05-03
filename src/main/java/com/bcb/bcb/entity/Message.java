package com.bcb.bcb.entity;


import com.bcb.bcb.enums.PriorityEnum;
import com.bcb.bcb.enums.StatusMessageEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "MESSAGE")
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Client sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Client recipient;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriorityEnum priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMessageEnum status;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal cost;


}
