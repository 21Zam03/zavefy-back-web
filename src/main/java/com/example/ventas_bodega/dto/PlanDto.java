package com.example.ventas_bodega.dto;

import com.example.ventas_bodega.enums.BillingPeriodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanDto {

    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private BillingPeriodEnum billingPeriod;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PlanFeatureDto> featuresDto;

}
