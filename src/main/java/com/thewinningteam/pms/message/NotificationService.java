package com.thewinningteam.pms.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.*;

@Service
public class NotificationService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void notifyDelivered(ChatMessage message) {
        try {
            message.setStatus(ChatMessage.Status.DELIVERED);
            chatMessageRepository.save(message);
            String jsonMessage = objectMapper.writeValueAsString(message);
            jmsTemplate.convertAndSend("notificationQueue", jsonMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send notification", e);
        }
    }

    public void notifyRead(ChatMessage message) {
        try {
            message.setStatus(ChatMessage.Status.READ);
            chatMessageRepository.save(message);
            String jsonMessage = objectMapper.writeValueAsString(message);
            jmsTemplate.convertAndSend("notificationQueue", jsonMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send notification", e);
        }
    }
}
