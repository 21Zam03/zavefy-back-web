package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.ReceivableDto;
import com.example.ventas_bodega.dto.interfaces.ReceivableDtoInter;
import com.example.ventas_bodega.entity.ReceivableEntity;

public class ReceivableMapper {

    public static ReceivableDto mapInterfaceToDto(ReceivableDtoInter receivableDtoInter) {
        if (receivableDtoInter == null) {
            return null;
        }
        ReceivableDto dto = new ReceivableDto();
        dto.setReceivableId(receivableDtoInter.getReceivableId());
        dto.setClientId(receivableDtoInter.getClientId());
        dto.setClientName(receivableDtoInter.getClientName());
        dto.setSaleId(receivableDtoInter.getSaleId());
        dto.setOriginalAmount(receivableDtoInter.getOriginalAmount());
        dto.setBalance(receivableDtoInter.getBalance());
        dto.setConcept(receivableDtoInter.getConcept());
        dto.setDueDate(receivableDtoInter.getDueDate());
        dto.setStatus(receivableDtoInter.getStatus());
        dto.setCreatedDate(receivableDtoInter.getCreatedDate());
        return dto;
    }

    public static ReceivableDto entityToDto(ReceivableEntity entity) {
        if (entity == null) {
            return null;
        }
        ReceivableDto dto = new ReceivableDto();
        dto.setReceivableId(entity.getReceivableId());
        dto.setClientId(entity.getClient() == null ? null : entity.getClient().getClientId());
        dto.setClientName(entity.getClient() == null ? null : entity.getClient().getFullName());
        dto.setSaleId(entity.getSaleId());
        dto.setOriginalAmount(entity.getOriginalAmount());
        dto.setBalance(entity.getBalance());
        dto.setConcept(entity.getConcept());
        dto.setDueDate(entity.getDueDate());
        dto.setStatus(entity.getStatus());
        dto.setCompanyId(entity.getCompanyId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

}
