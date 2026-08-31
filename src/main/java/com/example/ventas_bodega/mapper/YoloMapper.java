package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.YoloDetectionDto;
import com.example.ventas_bodega.dto.YoloDetectionResultDto;
import com.example.ventas_bodega.response.YoloDetectResponse;
import com.example.ventas_bodega.response.YoloDetectionResponse;

import java.util.List;
import java.util.stream.Collectors;

public class YoloMapper {

    public static YoloDetectionResultDto responseToDto(YoloDetectResponse response) {
        YoloDetectionResultDto dto = new YoloDetectionResultDto();
        dto.setTotal(response.getTotal());
        dto.setCounts(response.getCounts());
        dto.setDetections(detectionsToDto(response.getDetections()));
        return dto;
    }

    private static List<YoloDetectionDto> detectionsToDto(List<YoloDetectionResponse> detections) {
        if (detections == null) {
            return List.of();
        }
        return detections.stream()
                .map(d -> new YoloDetectionDto(d.getClassName(), d.getConfidence()))
                .collect(Collectors.toList());
    }

}
