package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.PaymentDto;
import com.example.ventas_bodega.entity.PaymentEntity;
import com.example.ventas_bodega.request.PaymentRequest;

public class PaymentMapper {

    public static PaymentDto entityToDto(PaymentEntity entity) {
        PaymentDto dto = new PaymentDto();
        dto.setId(entity.getId());
        dto.setSource(entity.getSource());
        dto.setNotificationId(entity.getNotificationId());
        dto.setSenderName(entity.getSenderName());
        dto.setAmount(entity.getAmount());
        dto.setSecurityCode(entity.getSecurityCode());
        dto.setReceivedAt(entity.getReceivedAt());
        dto.setStatus(entity.getStatus().toString());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static PaymentEntity requestToEntity(PaymentRequest request, Long companyId) {
        PaymentEntity entity = new PaymentEntity();
        entity.setCompanyId(companyId);
        entity.setSource(request.getSource());
        entity.setNotificationId(request.getNotificationId());
        entity.setSenderName(request.getSenderName());
        entity.setAmount(request.getAmount());
        entity.setSecurityCode(request.getSecurityCode());
        entity.setReceivedAt(request.getReceivedAt());
        entity.setFingerprint(request.getFingerprint());
        return entity;
    }

}
