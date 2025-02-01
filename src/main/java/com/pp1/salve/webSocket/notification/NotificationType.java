package com.pp1.salve.webSocket.notification;


public enum NotificationType {
    PEDIDO_NOVO, PEDIDO_ACEITO, PEDIDO_CANCELADO, PEDIDO_ATUALIZADO;

    public static NotificationType fromMessageType(MessageType messageType) {
        switch (messageType) {
            case PEDIDO_NOVO:
                return PEDIDO_NOVO;
            case PEDIDO_ACEITO:
                return PEDIDO_ACEITO;
            case PEDIDO_CANCELADO:
                return PEDIDO_CANCELADO;
            case PEDIDO_ATUALIZADO:
                return PEDIDO_ATUALIZADO;
            default:
                throw new IllegalArgumentException("Invalid message type");
        }
    }
}
