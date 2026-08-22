package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.PlanFeatureDto;
import com.example.ventas_bodega.entity.PlanFeatureEntity;

import java.util.ArrayList;
import java.util.List;

public class PlanFeatureMapper {

    public static PlanFeatureDto entityToDto(PlanFeatureEntity planFeatureEntity) {
        PlanFeatureDto planFeatureDto = new PlanFeatureDto();

        planFeatureDto.setPlanFeatureId(planFeatureEntity.getId());
        planFeatureDto.setPlanId(planFeatureEntity.getPlan().getId());
        planFeatureDto.setFeature(planFeatureEntity.getFeature());
        planFeatureDto.setFeatureType(planFeatureEntity.getFeatureType());
        planFeatureDto.setValue(planFeatureEntity.getValue());
        planFeatureDto.setCreatedAt(planFeatureEntity.getCreatedAt());
        planFeatureDto.setUpdatedAt(planFeatureEntity.getUpdatedAt());
        planFeatureDto.setDescription(planFeatureEntity.getDescription());

        return planFeatureDto;
    }

    public static List<PlanFeatureDto> entityListToDtoList(
            List<PlanFeatureEntity> planFeatureEntityList) {

        if (!planFeatureEntityList.isEmpty()) {
            List<PlanFeatureDto> planFeatureDtoList = new ArrayList<>();

            for (PlanFeatureEntity planFeatureEntity : planFeatureEntityList) {
                PlanFeatureDto planFeatureDto =
                        PlanFeatureMapper.entityToDto(planFeatureEntity);

                planFeatureDtoList.add(planFeatureDto);
            }

            return planFeatureDtoList;
        } else {
            return null;
        }
    }

}
