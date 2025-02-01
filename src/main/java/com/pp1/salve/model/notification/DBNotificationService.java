package com.pp1.salve.model.notification;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DBNotificationService {
    final private NotificationRepository notificationRepository;
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }
    public void delete(Notification notification) {
        notificationRepository.delete(notification);
    }
    public void deleteAll(List<Notification> notifications) {
        notificationRepository.deleteAll(notifications);
    }
    public List<Notification> findByReceiverId(String receiverId) {
        List<Notification> notifications = notificationRepository.findByReceiverId(receiverId);
        return notifications;
    }
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }
}
