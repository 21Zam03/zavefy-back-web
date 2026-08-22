package com.example.ventas_bodega.dto;

import com.example.ventas_bodega.entity.PlanEntity;
import com.example.ventas_bodega.enums.FeatureTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanFeatureDto {

    private Long planFeatureId;
    private Integer planId;
    private String feature;
    private FeatureTypeEnum featureType;
    private String value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;

}
