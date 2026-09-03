package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.ReceivablePaymentDto;
import com.example.ventas_bodega.dto.interfaces.ReceivablePaymentDtoInter;
import com.example.ventas_bodega.entity.ReceivablePaymentEntity;

import java.util.ArrayList;
import java.util.List;

public class ReceivablePaymentMapper {

    public static ReceivablePaymentDto interToDto(ReceivablePaymentDtoInter inter) {
        if (inter == null) {
            return null;
        }
        ReceivablePaymentDto dto = new ReceivablePaymentDto();
        dto.setPaymentId(inter.getPaymentId());
        dto.setReceivableId(inter.getReceivableId());
        dto.setAmount(inter.getAmount());
        dto.setPaymentMethod(inter.getPaymentMethod());
        dto.setPaymentDate(inter.getPaymentDate());
        dto.setCreatedBy(inter.getCreatedBy());
        dto.setClientName(inter.getClientName());
        dto.setRegisteredByName(inter.getRegisteredByName());
        return dto;
    }

    public static ReceivablePaymentDto entityToDto(ReceivablePaymentEntity entity) {
        if (entity == null) {
            return null;
        }
        ReceivablePaymentDto dto = new ReceivablePaymentDto();
        dto.setPaymentId(entity.getPaymentId());
        dto.setReceivableId(entity.getReceivable() == null ? null : entity.getReceivable().getReceivableId());
        dto.setAmount(entity.getAmount());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setCreatedBy(entity.getCreatedBy());
        return dto;
    }

    public static List<ReceivablePaymentDto> entityListToDtoList(List<ReceivablePaymentEntity> entityList) {
        List<ReceivablePaymentDto> dtoList = new ArrayList<>();
        for (ReceivablePaymentEntity entity : entityList) {
            dtoList.add(entityToDto(entity));
        }
        return dtoList;
    }

}
