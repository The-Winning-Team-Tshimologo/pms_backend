package com.thewinningteam.pms.message;

import com.thewinningteam.pms.message.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndRecipientOrderByTimestampAsc(String sender, String recipient);
    List<ChatMessage> findByRecipientAndSenderOrderByTimestampAsc(String recipient, String sender);

    List<ChatMessage> findBySenderOrRecipientOrderByTimestampAsc(String userName, String userName1);
}