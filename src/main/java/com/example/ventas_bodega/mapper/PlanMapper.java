package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.PlanDto;
import com.example.ventas_bodega.entity.PlanEntity;
import com.example.ventas_bodega.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class PlanMapper {

    public static PlanDto entityToDto(PlanEntity planEntity) {
        PlanDto planDto = new PlanDto();
        planDto.setId(planEntity.getId());
        planDto.setName(planDto.getName());
        planDto.setDescription(planDto.getDescription());
        planDto.setPrice(planDto.getPrice());
        planDto.setCurrency(planDto.getCurrency());
        planDto.setBillingPeriod(planEntity.getBillingPeriod());
        planDto.setActive(planEntity.getActive());
        planDto.setCreatedAt(planEntity.getCreatedAt());
        planDto.setUpdatedAt(planEntity.getUpdatedAt());
        return planDto;
    }

    public static List<PlanDto> entityListToDtoList(List<PlanEntity> planEntityList) {
        if(!planEntityList.isEmpty()) {
            List<PlanDto> planDtoList = new ArrayList<>();
            for(PlanEntity planEntity : planEntityList) {
                PlanDto planDto = PlanMapper.entityToDto(planEntity);
                planEntityList.add(planEntity);
                planDtoList.add(planDto);
            }
            return planDtoList;
        } else return null;
    }


}
