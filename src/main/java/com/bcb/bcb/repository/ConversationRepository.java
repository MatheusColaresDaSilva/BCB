package com.bcb.bcb.repository;

import com.bcb.bcb.entity.Conversation;
import com.bcb.bcb.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

}
