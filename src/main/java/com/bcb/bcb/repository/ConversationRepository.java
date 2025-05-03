package com.bcb.bcb.repository;

import com.bcb.bcb.entity.Conversation;
import com.bcb.bcb.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByClientId(Long clientId);
}
