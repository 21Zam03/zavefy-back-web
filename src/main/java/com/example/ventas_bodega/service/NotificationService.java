package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.NotificationDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface NotificationService {

    void checkReceivablesDueSoon();
    void checkReceivablesOverdue();
    void checkLowStock();
    void checkOutOfStock();
    void checkOpenCajaTooLong();

    Page<NotificationDto> getNotifications(UserEntity user, Boolean unreadOnly, int page, int size);
    long countUnread(UserEntity user);
    MessageResponse markAsRead(Long notificationId, UserEntity user);
    MessageResponse markAllAsRead(UserEntity user);

}
