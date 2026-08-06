package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.OpportunityDto;
import com.example.ventas_bodega.entity.OpportunityEntity;

public class OpportunityMapper {

    public static OpportunityDto entityToDto(OpportunityEntity entity) {
        if (entity == null) {
            return null;
        }

        OpportunityDto dto = new OpportunityDto();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setStatus(entity.getStatus());

        dto.setPriority(
                entity.getPriority() != null
                        ? entity.getPriority().name()
                        : null
        );

        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setFullName(entity.getFullName());

        dto.setAssignedToId(
                entity.getAssignedTo() != null
                        ? entity.getAssignedTo().getUserId().longValue()
                        : null
        );

        dto.setSource(
                entity.getSource() != null
                        ? entity.getSource().name()
                        : null
        );

        dto.setSubject(
                entity.getSubject() != null
                        ? entity.getSubject().name()
                        : null
        );

        dto.setMessage(entity.getMessage());

        dto.setResult(
                entity.getResult() != null
                        ? entity.getResult().name()
                        : null
        );

        dto.setLostReason(entity.getLostReason());
        dto.setInternalNotes(entity.getInternalNotes());

        dto.setCreatedDate(entity.getCreatedDate());
        dto.setRegisterDate(entity.getRegisterDate());

        // Cuando implementes OpportunityDetailMapper
        // dto.setDetails(
        //     entity.getDetails()
        //           .stream()
        //           .map(OpportunityDetailMapper::entityToDto)
        //           .toList()
        // );

        return dto;
    }

}
