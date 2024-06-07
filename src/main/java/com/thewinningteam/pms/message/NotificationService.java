package com.thewinningteam.pms.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final NotificationRepository notificationRepository;

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

    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setStatus(NotificationStatus.READ);
        notificationRepository.save(notification);
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public Long getUnreadCount(String username) {
        return notificationRepository.countByRecipientAndStatus(username, NotificationStatus.UNREAD);
    }

    public List<Notification> getNotificationsForCustomer(String recipient) {
        return notificationRepository.findByRecipient(recipient);
    }

}
