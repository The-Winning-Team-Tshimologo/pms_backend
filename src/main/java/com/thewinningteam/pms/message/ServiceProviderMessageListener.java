package com.thewinningteam.pms.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class ServiceProviderMessageListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private NotificationService notificationService;

    @JmsListener(destination = "serviceProviderQueue")
    public void receiveMessage(String jsonMessage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SERVICE_PROVIDER"))) {
                try {
                    ChatMessage message = objectMapper.readValue(jsonMessage, ChatMessage.class);
                    chatMessageRepository.save(message);
                    notificationService.notifyDelivered(message);
                    System.out.println("ServiceProvider received: " + message);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to convert JSON message to ChatMessage", e);
                }
            } else {
                throw new AccessDeniedException("Unauthorized access");
            }
        } else {
            throw new InsufficientAuthenticationException("Authentication is required");
        }
    }
}
