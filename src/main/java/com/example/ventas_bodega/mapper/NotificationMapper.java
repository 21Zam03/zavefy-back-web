package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.NotificationDto;
import com.example.ventas_bodega.entity.NotificationEntity;

public class NotificationMapper {

    public static NotificationDto entityToDto(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }
        NotificationDto dto = new NotificationDto();
        dto.setNotificationId(entity.getNotificationId());
        dto.setMessage(entity.getMessage());
        dto.setRead(entity.isRead());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUserId(entity.getUserId());
        dto.setCompanyId(entity.getCompanyId());
        dto.setType(entity.getType());
        dto.setSeverity(entity.getSeverity());
        dto.setReferenceType(entity.getReferenceType());
        dto.setReferenceId(entity.getReferenceId());
        return dto;
    }

}
