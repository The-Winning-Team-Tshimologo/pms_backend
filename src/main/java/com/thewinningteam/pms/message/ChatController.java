package com.thewinningteam.pms.message;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;


    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SERVICE_PROVIDER')")
    @PostMapping("/send/{destination}")
    public void sendMessage(@PathVariable String destination, @RequestBody ChatMessage message, Authentication authentication) {
        chatService.sendMessage(destination, message, authentication);
    }


    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SERVICE_PROVIDER')")
    @GetMapping("/conversations-per-user")
    public Map<String, List<ChatMessage>> getConversationsPerUser() {
        return chatService.getChatsPerUser();
    }
}


