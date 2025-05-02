package com.bcb.bcb.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Conversation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Client recipient;

    @Column(name = "unreadcount", nullable = false)
    private int unreadCount;


}
