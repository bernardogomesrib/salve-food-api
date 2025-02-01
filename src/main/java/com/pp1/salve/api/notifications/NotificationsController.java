package com.pp1.salve.api.notifications;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.model.notification.DBNotificationService;
import com.pp1.salve.model.notification.Notification;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationsController {
    private final DBNotificationService dbNotificationService;
    @GetMapping
    public List<Notification> getNotifications(Authentication authentication) {
        return dbNotificationService.findByReceiverId(authentication.getName());
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        dbNotificationService.deleteById(id);
    }
    
}
