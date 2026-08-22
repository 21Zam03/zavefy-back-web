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
        planDto.setName(planEntity.getName());
        planDto.setDescription(planEntity.getDescription());
        planDto.setPrice(planEntity.getPrice());
        planDto.setCurrency(planEntity.getCurrency());
        planDto.setBillingPeriod(planEntity.getBillingPeriod());
        planDto.setActive(planEntity.getActive());
        planDto.setCreatedAt(planEntity.getCreatedAt());
        planDto.setUpdatedAt(planEntity.getUpdatedAt());
        planDto.setFeaturesDto(PlanFeatureMapper.entityListToDtoList(planEntity.getFeatures()));
        return planDto;
    }

    public static List<PlanDto> entityListToDtoList(List<PlanEntity> planEntityList) {
        if(!planEntityList.isEmpty()) {
            List<PlanDto> planDtoList = new ArrayList<>();
            for(PlanEntity planEntity : planEntityList) {
                PlanDto planDto = PlanMapper.entityToDto(planEntity);
                planDtoList.add(planDto);
            }
            return planDtoList;
        } else return null;
    }


}
