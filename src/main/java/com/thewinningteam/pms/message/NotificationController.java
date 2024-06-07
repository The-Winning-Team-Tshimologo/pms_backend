package com.thewinningteam.pms.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    public List<Notification> getNotifications(Authentication authentication) {
        String username = authentication.getName();
        return notificationService.getNotificationsForCustomer(username);
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SERVICE_PROVIDER')")
    @GetMapping("/unread-count")
    public Long getUnreadCount(Authentication authentication) {
        String username = authentication.getName();
        return notificationService.getUnreadCount(username);
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SERVICE_PROVIDER')")
    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}
