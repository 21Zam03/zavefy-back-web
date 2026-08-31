package com.example.ventas_bodega.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YoloDetectionDto {

    private String className;
    private Double confidence;

}
