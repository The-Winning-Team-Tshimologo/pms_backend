package com.thewinningteam.pms.message;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.thewinningteam.pms.Repository.UserRepository;
import com.thewinningteam.pms.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendMessage(String destination, ChatMessage message, Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userEmail = userDetails.getUsername();

            Optional<User> recipient = userRepository.findByUserName(destination);
            if (recipient.isEmpty()) {
                throw new IllegalArgumentException("Recipient does not exist");
            }

            message.setSender(userEmail);
            message.setTimestamp(LocalDateTime.now());
            message.setRecipient(destination);
            message.setStatus(ChatMessage.Status.SENT);
            chatMessageRepository.save(message);

            String jsonMessage = objectMapper.writeValueAsString(message);
            jmsTemplate.convertAndSend(destination, jsonMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    public void updateMessageStatus(Long messageId, ChatMessage.Status status) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        message.setStatus(status);
        chatMessageRepository.save(message);
    }

    public List<ChatMessage> getConversation(Authentication authentication, String user2) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        List<ChatMessage> messages = chatMessageRepository.findBySenderAndRecipientOrderByTimestampAsc(userName, user2);
        messages.addAll(chatMessageRepository.findByRecipientAndSenderOrderByTimestampAsc(user2, userName));
        messages.sort(Comparator.comparing(ChatMessage::getTimestamp));
        return messages;
    }

    public Map<String, List<ChatMessage>> getChatsPerUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();

        List<ChatMessage> allChats = chatMessageRepository.findBySenderOrRecipientOrderByTimestampAsc(userName, userName);

        Map<String, List<ChatMessage>> chatsPerUser = new HashMap<>();
        for (ChatMessage message : allChats) {
            String otherUser = message.getSender().equals(userName) ? message.getRecipient() : message.getSender();
            if (otherUser != null) {
                chatsPerUser.computeIfAbsent(otherUser, k -> new ArrayList<>()).add(message);
            }
        }
        return chatsPerUser;
    }

}
