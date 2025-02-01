package com.pp1.salve.model.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
   List<Notification>findByReceiverId(String receiverId); 
   
}
