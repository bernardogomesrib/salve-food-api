package com.pp1.salve.webSocket.notification;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    private String senderName;
    private String message;
    private String receverId;
    private NotificationType notificationType;
    private Long pedidoId;

    public static com.pp1.salve.model.notification.Notification toEntity(Notification notification) {
        return com.pp1.salve.model.notification.Notification.builder()
                .senderName(notification.getSenderName())
                .message(notification.getMessage())
                .receiverId(notification.getReceverId())
                .notificationType(notification.getNotificationType().name())
                .pedidoId(notification.getPedidoId())
                .build();
    }
}
