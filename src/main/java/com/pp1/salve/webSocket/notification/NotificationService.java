package com.pp1.salve.webSocket.notification;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(String userId, Notification notification) {
        log.info("Sending notification to user: {} with payload: {}", userId, notification);
        simpMessagingTemplate.convertAndSendToUser(userId,"/notification", notification);
    }
}
