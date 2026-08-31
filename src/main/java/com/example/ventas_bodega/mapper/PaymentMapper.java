package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.entity.PaymentEntity;
import com.example.ventas_bodega.request.PaymentRequest;

public class PaymentMapper {

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
