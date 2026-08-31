package com.example.ventas_bodega.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YoloDetectionResultDto {

    private int total;
    private Map<String, Integer> counts;
    private List<YoloDetectionDto> detections;

}
