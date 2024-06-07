package com.thewinningteam.pms.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NotificationListener {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @JmsListener(destination = "notificationQueue")
    public void receiveMessage(String jsonMessage) {
        try {
            Notification notification = objectMapper.readValue(jsonMessage, Notification.class);
            // Process the notification object
            notificationRepository.save(notification);
            System.out.println("Received and saved notification: " + notification.getContent());
        } catch (IOException e) {
            // Handle deserialization error
            e.printStackTrace();
        }
    }

}

